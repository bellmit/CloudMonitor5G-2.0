package com.datang.service.influx;

import com.datang.dao.quesroad.QuesRoadDao;
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.service.influx.bean.QuesRoadThreshold;
import com.datang.service.testLogItem.UnicomLogItemService;
import com.datang.util.AdjPlaneArithmetic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 问题路段处理类
 */
@Service
public class QuesRoadProcessor {
    @Autowired
    private QuesRoadDao quesRoadDao;
    @Autowired
    private InfluxService influxService;
    @Autowired
    private UnicomLogItemService unicomLogItemService;
    //问题路段获取基础采样点模板sql
    private static String BASE_ROAD_SAMP_SQL="SELECT Long,Lat,IEValue_51192,IEValue_51193,IEValue_53432,IEValue_53431,IEValue_50087,IEValue_53434,IEValue_53433,IEValue_50007,IEValue_71053,IEValue_71054,IEValue_71051,IEValue_53419,IEValue_71052,IEValue_54572,IEValue_53483,IEValue_54231,IEValue_50097,IEValue_50055,IEValue_50990,IEValue_53456,IEValue_53601,IEValue_50056,IEValue_50991,IEValue_50014,IEValue_53457,IEValue_74214,IEValue_71000,IEValue_73001,IEValue_73100,IEValue_73000 FROM IE where  Long!='-1' and Lat!='-1' ";
    private static Map<String,String[]> WHERE_MAP=new HashMap<>();
    private static Map<String,String[]> EVTS_MAP=new HashMap<>();
    static {
        WHERE_MAP.put("上行质差路段",new String[]{"IEValue_50097","IEValue_54333"});
        WHERE_MAP.put("下行质差路段",new String[]{"IEValue_50055","IEValue_50056"});
        WHERE_MAP.put("弱覆盖路段",new String[]{"IEValue_50055"});
        WHERE_MAP.put("上行低速率路段",new String[]{"IEValue_54231"});
        WHERE_MAP.put("下行低速率路段",new String[]{"IEValue_53483"});
        EVTS_MAP.put("上行低速率路段",new String[]{"Ftp Upload Attempt","Ftp Upload Success","FTP UpLoad Drop"});
        EVTS_MAP.put("下行低速率路段",new String[]{"Ftp Upload Attempt","Ftp Upload Success","FTP UpLoad Drop"});
    }

    /**
     * 问题路段分析入口
     * @param fileLogIds
     */
    public Map<String, List<Map<String, Object>>> analysize(List<String> fileLogIds){
        List<TestLogItem> testLogItems = unicomLogItemService.queryTestLogItems(fileLogIds.stream().collect(Collectors.joining(",")));
        Map<Long, TestLogItem> id2LogBeanMap = testLogItems.stream().collect(Collectors.toMap(TestLogItem::getRecSeqNo, Function.identity()));
        List<QuesRoadThreshold> quesRoadThresholds = quesRoadDao.queryAll();
        Map<String,Double> thresholdMap=quesRoadThresholds.stream().collect(Collectors.toMap(QuesRoadThreshold::getName,QuesRoadThreshold::getValue));
        Map<String, List<Map<String, Object>>> result=Collections.synchronizedMap(new HashMap<>());
        fileLogIds.stream().forEach(id->{
            WHERE_MAP.entrySet().parallelStream().forEach(entry->{
                String key=entry.getKey();
                List<Map<String, Object>> sampDatas;
                //有事件条件的处理
                if(EVTS_MAP.containsKey(key)){
                    List<Map<String, Object>> evtResults = influxService.evtPointTimes(Long.parseLong(id), EVTS_MAP.get(key));
                    if(existEvt(evtResults,"Ftp Upload Attempt")){
                        List<Map<String,String>> times=getTimeIntervals(evtResults);
                        for(Map<String,String> time:times){
                            sampDatas = influxService.queryRoadSampDatas(BASE_ROAD_SAMP_SQL, Long.parseLong(id), Arrays.asList(time));
                            Map<String, List<Map<String, Object>>> tempR=quesRoadAlgorithm(key,sampDatas,thresholdMap,id2LogBeanMap.get(Long.parseLong(id)));
                            if(result.containsKey(key)){
                                result.get(key).addAll(tempR.get(key));
                            }else{
                                result.putAll(tempR);
                            }
                        }
                    }
                }else{
                    sampDatas=influxService.queryRoadSampDatas(BASE_ROAD_SAMP_SQL, Long.parseLong(id), Collections.emptyList());
                    //问题路段算法
                    Map<String, List<Map<String, Object>>> tempR=quesRoadAlgorithm(key,sampDatas,thresholdMap,id2LogBeanMap.get(Long.parseLong(id)));
                    result.putAll(tempR);
                }

            });

        });
        return result;
    }

    /**
     * 问题路段算法
     * @param key
     * @param sampDatas
     * @param thresholdMap
     * @param testLogItem
     * @return
     */
    private Map<String, List<Map<String, Object>>> quesRoadAlgorithm(String key, List<Map<String, Object>> sampDatas, Map<String, Double> thresholdMap, TestLogItem testLogItem) {
        Map<String, Object> point = sampDatas.get(0);
        Double lon=Double.parseDouble(point.get("Long").toString());
        Double lat=Double.parseDouble(point.get("Lat").toString());
        List<int[]> quesRaods=new ArrayList<>();
        List<Map<String, Object>> slideWindow=new ArrayList<>();
        int start=0,end=0;//问题路段的起点和终点index
        boolean flag=true;//新路段开启标志
        for(int i=0;i<sampDatas.size();i++){
            Map<String, Object> point2 = sampDatas.get(i);
            Double lon2=Double.parseDouble(point2.get("Long").toString());
            Double lat2=Double.parseDouble(point2.get("Lat").toString());
            Double distance = AdjPlaneArithmetic.getDistance(lon, lat, lon2, lat2);
            if("弱覆盖路段".equalsIgnoreCase(key)){
                if(distance>=thresholdMap.get("weakcoverroadlen")){
                    Double rate=slideWindow.stream().filter(getMapPredicate2(thresholdMap, "IEValue_50055", "weakcoverrsrp")).count()*1.0/slideWindow.size();
                    Ss ss = new Ss(thresholdMap, quesRaods, slideWindow, start, end, flag, i, rate).invoke("weakcoversamprate");
                    start = ss.getStart();
                    end = ss.getEnd();
                    flag = ss.isFlag();
                    slideWindow.remove(0);
                    slideWindow.add(sampDatas.get(i));
                }else{
                    slideWindow.add(sampDatas.get(i));
                    end++;
                }
            }
            if("上行质差路段".equalsIgnoreCase(key)){
                if(distance>=thresholdMap.get("upqualitydiffroadlen")){
                    Double rate=slideWindow.stream().filter(getMapPredicate1(thresholdMap)).count()*1.0/slideWindow.size();
                    Ss ss = new Ss(thresholdMap, quesRaods, slideWindow, start, end, flag, i, rate).invoke("upqualitydiffsamprate");
                    start = ss.getStart();
                    end = ss.getEnd();
                    flag = ss.isFlag();
                    slideWindow.remove(0);
                    slideWindow.add(sampDatas.get(i));
                }else{
                    slideWindow.add(sampDatas.get(i));
                    end++;
                }
            }
            if("下行质差路段".equalsIgnoreCase(key)){
                if(distance>=thresholdMap.get("downqualitydiffroadlen")){
                    Double rate=slideWindow.stream().filter(getMapPredicate(thresholdMap)).count()*1.0/slideWindow.size();
                    Ss ss = new Ss(thresholdMap, quesRaods, slideWindow, start, end, flag, i, rate).invoke("downqualitydiffsamprate");
                    start = ss.getStart();
                    end = ss.getEnd();
                    flag = ss.isFlag();
                    slideWindow.remove(0);
                    slideWindow.add(sampDatas.get(i));
                }else{
                    slideWindow.add(sampDatas.get(i));
                    end++;
                }
            }
            if("上行低速率路段".equalsIgnoreCase(key)){
                if(distance>=thresholdMap.get("uplowerspeedroadlen")){
                    Double rate=slideWindow.stream().filter(getMapPredicate2(thresholdMap, "IEValue_54231", "uplowerspeedrlc")).count()*1.0/slideWindow.size();
                    Ss ss = new Ss(thresholdMap, quesRaods, slideWindow, start, end, flag, i, rate).invoke("uplowerspeedsamprate");
                    start = ss.getStart();
                    end = ss.getEnd();
                    flag = ss.isFlag();
                    slideWindow.remove(0);
                    slideWindow.add(sampDatas.get(i));
                }else{
                    slideWindow.add(sampDatas.get(i));
                    end++;
                }
            }
            if("下行低速率路段".equalsIgnoreCase(key)){
                if(distance>=thresholdMap.get("downlowerspeedroadlen")){
                    Double rate=slideWindow.stream().filter(getMapPredicate2(thresholdMap, "IEValue_53483", "downlowerspeedrlc")).count()*1.0/slideWindow.size();
                    Ss ss = new Ss(thresholdMap, quesRaods, slideWindow, start, end, flag, i, rate).invoke("downlowerspeedsamprate");
                    start = ss.getStart();
                    end = ss.getEnd();
                    flag = ss.isFlag();
                    slideWindow.remove(0);
                    slideWindow.add(sampDatas.get(i));
                }else{
                    slideWindow.add(sampDatas.get(i));
                    end++;
                }
            }
        }
        Map<String,List<Map<String,Object>>> result=new HashMap<>();
        List<Map<String,Object>> list=new ArrayList<>();
        quesRaods.forEach(item->{
            List<Map<String, Object>> maps = sampDatas.subList(item[0], item[1] + 1);
            Map<String,Object> obj=new HashMap<>();
            obj.put("logname",testLogItem.getFileName());
            obj.put("starttime",maps.get(0).get("time"));
            double slong = Double.parseDouble(maps.get(0).get("Long").toString());
            double slat = Double.parseDouble(maps.get(0).get("Lat").toString());
            obj.put("slong", slong);
            obj.put("slat",slat);
            obj.put("endtime",maps.get(maps.size()-1).get("time"));
            double elong = Double.parseDouble(maps.get(maps.size()-1).get("Long").toString());
            double elat = Double.parseDouble(maps.get(maps.size()-1).get("Lat").toString());
            obj.put("elong", slong);
            obj.put("elat",slat);
            obj.put("elong",elong);
            obj.put("elat",elat);
            obj.put("totalLen",getSumKm(maps));
            Map<String,Integer> pciCountMap=new HashMap<>();
            maps.stream().filter(i->i.get("IEValue_50007")!=null).collect(Collectors.groupingBy(i -> i.get("IEValue_50007").toString())).forEach((a,b)->{
                pciCountMap.put(a,b.size());
            });
            List<Map.Entry<String, Integer>> collect = pciCountMap.entrySet().stream().sorted(Comparator.comparingInt(Map.Entry::getValue)).collect(Collectors.toList());
            obj.put("pci1",collect.get(0).getKey());
            obj.put("pci1count",collect.get(0).getValue());
            obj.put("pci2",collect.get(1).getKey());
            obj.put("pci2count",collect.get(1).getValue());
            obj.put("pci3",collect.get(1).getKey());
            obj.put("pci3count",collect.get(1).getValue());
            obj.put("sumrsrp",InfluxReportUtils.getSumKpi2(maps,"IEValue_50055"));
            obj.put("countrsrp",InfluxReportUtils.getCountKpi2(maps,"IEValue_50055"));
            obj.put("sumsinr",InfluxReportUtils.getSumKpi2(maps,"IEValue_50056"));
            obj.put("countsinr",InfluxReportUtils.getCountKpi2(maps,"IEValue_50056"));

            obj.put("sumdistance",InfluxReportUtils.getSumKpi2(maps,"IEValue_50014"));
            obj.put("countdistance",InfluxReportUtils.getCountKpi2(maps,"IEValue_50014"));
            obj.put("maxdistance",InfluxReportUtils.getMaxKpi(maps,"IEValue_50014"));
            obj.put("mindistance",InfluxReportUtils.getMinKpi(maps,"IEValue_50014"));

            Map<String,Integer> arfcnCountMap=new HashMap<>();
            maps.stream().filter(i->i.get("IEValue_53601")!=null).collect(Collectors.groupingBy(i -> i.get("IEValue_53601").toString())).forEach((a,b)->{
                arfcnCountMap.put(a,b.size());
            });
            List<Map.Entry<String, Integer>> collect1 = pciCountMap.entrySet().stream().sorted(Comparator.comparingInt(Map.Entry::getValue)).collect(Collectors.toList());
            obj.put("arfcn1",collect1.get(0).getKey());
            obj.put("arfcncount",collect1.get(0).getValue());
            obj.put("arfcn2",collect1.get(1).getKey());
            obj.put("arfcn2count",collect1.get(1).getValue());
            obj.put("arfcn3",collect1.get(1).getKey());
            obj.put("arfcn3count",collect1.get(1).getValue());

            obj.put("sumdlinitbler",InfluxReportUtils.getSumKpi2(maps,"IEValue_73000"));
            obj.put("countdlinitbler",InfluxReportUtils.getCountKpi2(maps,"IEValue_73000"));

            obj.put("sumdlresibler",InfluxReportUtils.getSumKpi2(maps,"IEValue_73001"));
            obj.put("countdlresibler",InfluxReportUtils.getCountKpi2(maps,"IEValue_73001"));

            obj.put("sumdtxpower",InfluxReportUtils.getSumKpi2(maps,"IEValue_50097"));
            obj.put("counttxpower",InfluxReportUtils.getCountKpi2(maps,"IEValue_50097"));

            obj.put("sumulinitbler",InfluxReportUtils.getSumKpi2(maps,"IEValue_54572"));
            obj.put("countulinitbler",InfluxReportUtils.getCountKpi2(maps,"IEValue_54572"));

            obj.put("sumulresibler",InfluxReportUtils.getSumKpi2(maps,"IEValue_71000"));
            obj.put("countulresibler",InfluxReportUtils.getCountKpi2(maps,"IEValue_71000"));

            obj.put("sum53483",InfluxReportUtils.getSumKpi2(maps,"IEValue_53483"));
            obj.put("count53483",InfluxReportUtils.getCountKpi2(maps,"IEValue_53483"));
            Predicate<Map<String,Object>> predicate=stringObjectMap->Double.parseDouble(stringObjectMap.get("IEValue_53483").toString())<=100;
            obj.put("count53483lt100",InfluxReportUtils.getCountFilterKpi2(maps,"IEValue_53483",predicate));
            obj.put("sum50990",InfluxReportUtils.getSumKpi2(maps,"IEValue_50990"));
            obj.put("sum53419",InfluxReportUtils.getSumKpi2(maps,"IEValue_53419"));
            obj.put("count53419",InfluxReportUtils.getCountKpi2(maps,"IEValue_53419"));
            obj.put("sum51192",InfluxReportUtils.getSumKpi2(maps,"IEValue_51192"));
            obj.put("count51192",InfluxReportUtils.getCountKpi2(maps,"IEValue_51192"));
            obj.put("sum74214",InfluxReportUtils.getSumKpi2(maps,"IEValue_74214"));
            obj.put("count74214",InfluxReportUtils.getCountKpi2(maps,"IEValue_74214"));
            obj.put("sum53456",InfluxReportUtils.getSumKpi2(maps,"IEValue_53456"));
            obj.put("count53456",InfluxReportUtils.getCountKpi2(maps,"IEValue_53456"));
            obj.put("sum53434",InfluxReportUtils.getSumKpi2(maps,"IEValue_53434"));
            obj.put("sum53433",InfluxReportUtils.getSumKpi2(maps,"IEValue_53433"));
            obj.put("sum53432",InfluxReportUtils.getSumKpi2(maps,"IEValue_53434"));
            obj.put("sum53431",InfluxReportUtils.getSumKpi2(maps,"IEValue_53433"));
            obj.put("sum50087",InfluxReportUtils.getSumKpi2(maps,"IEValue_50087"));
            obj.put("count50087",InfluxReportUtils.getCountKpi2(maps,"IEValue_50087"));
            obj.put("sum54231",InfluxReportUtils.getSumKpi2(maps,"IEValue_54231"));
            obj.put("count54231",InfluxReportUtils.getCountKpi2(maps,"IEValue_54231"));
            Predicate<Map<String,Object>> predicate1=stringObjectMap->Double.parseDouble(stringObjectMap.get("IEValue_54231").toString())<5;
            obj.put("count54231lt5",InfluxReportUtils.getCountFilterKpi2(maps,"IEValue_54231",predicate1));
            obj.put("sum50991",InfluxReportUtils.getSumKpi2(maps,"IEValue_50991"));
            obj.put("sum73100",InfluxReportUtils.getSumKpi2(maps,"IEValue_73100"));
            obj.put("count73100",InfluxReportUtils.getCountKpi2(maps,"IEValue_73100"));
            obj.put("sum51193",InfluxReportUtils.getSumKpi2(maps,"IEValue_51193"));
            obj.put("count51193",InfluxReportUtils.getCountKpi2(maps,"IEValue_51193"));
            obj.put("sum53457",InfluxReportUtils.getSumKpi2(maps,"IEValue_53457"));
            obj.put("count53457",InfluxReportUtils.getCountKpi2(maps,"IEValue_53457"));
            obj.put("sum71054",InfluxReportUtils.getSumKpi2(maps,"IEValue_71054"));
            obj.put("sum71053",InfluxReportUtils.getSumKpi2(maps,"IEValue_71053"));
            obj.put("sum71052",InfluxReportUtils.getSumKpi2(maps,"IEValue_71052"));
            obj.put("sum71051",InfluxReportUtils.getSumKpi2(maps,"IEValue_71051"));
            list.add(obj);
        });
        result.put(key,list);
        return result;
    }

    private Object getSumKm(List<Map<String,Object>> maps) {
        Double sumkm=0.0;
        for(int i=0;i<maps.size()-1;i++){
            double slong = Double.parseDouble(maps.get(i).get("Long").toString());
            double slat = Double.parseDouble(maps.get(i).get("Lat").toString());
            double elong = Double.parseDouble(maps.get(i+1).get("Long").toString());
            double elat = Double.parseDouble(maps.get(i+1).get("Lat").toString());
            sumkm+=AdjPlaneArithmetic.getDistance(slong, slat, elong, elat);
        }
        return sumkm;
    }

    private Predicate<Map<String, Object>> getMapPredicate2(Map<String, Double> thresholdMap, String ieValue_50055, String weakcoverrsrp) {
        return item -> null!=item.get(ieValue_50055)&&Double.parseDouble(item.get(ieValue_50055).toString()) <= thresholdMap.get(weakcoverrsrp);
    }

    private Predicate<Map<String, Object>> getMapPredicate1(Map<String, Double> thresholdMap) {
        return item->null!=item.get("IEValue_50097")&&null!=item.get("IEValue_54333")&&Double.parseDouble(item.get("IEValue_50097").toString())>=thresholdMap.get("upqualitydiffsamprate")&&Double.parseDouble(item.get("IEValue_54333").toString())>=thresholdMap.get("upqualitydiffbler");
    }

    private Predicate<Map<String, Object>> getMapPredicate(Map<String, Double> thresholdMap) {
        return item->null!=item.get("IEValue_50056")&&null!=item.get("IEValue_50055")&&Double.parseDouble(item.get("IEValue_50055").toString())<=thresholdMap.get("upqualitydiffsamprate")&&Double.parseDouble(item.get("IEValue_50056").toString())<=thresholdMap.get("downqualitydiffrsrp");
    }

    /**
     * 按照“Ftp Upload Attempt” 切分时间段
     * @param evtResults
     * @return
     */
    private List<Map<String,String>> getTimeIntervals(List<Map<String,Object>> evtResults) {
        List<Map<String,String>> r=new ArrayList<>();
        String startTime;
        String endTime;
        for(int i=0;i<evtResults.size();i++){
            if("Ftp Upload Attempt".equalsIgnoreCase(evtResults.get(i).get("evtName").toString())){
                Map<String,String> item=new HashMap<>();
                startTime=evtResults.get(i).get("time").toString();
                item.put("startTime",startTime);
                if(evtResults.size()>i+1){
                    Map<String, Object> map1 = evtResults.get(i + 1);
                    if("Ftp Upload Attempt".equalsIgnoreCase(map1.get("evtName").toString())){
                        continue;
                    }else{
                        endTime=map1.get("time").toString();
                        item.put("endTime",endTime);
                    }
                }
                r.add(item);
            }
        }
        return r;
    }

    /*public static void main(String[] args) {
        String s="AAAAAAA";
        List<String> r=new ArrayList<>();
        for(int i=0;i<s.length();i++){
            char m=s.charAt(i);
            StringBuilder sb=new StringBuilder();
            if('A'==m){
                sb.append(m);
                if(s.length()>i+1){
                    char n = s.charAt(i+1);
                    if('A'==n){
                        continue;
                    }else{
                        sb.append(n);
                    }
                }
                r.add(sb.toString());
            }
        }
        r.forEach(item->{
            System.out.println(item);
        });
    }*/




    /**
     * 是否存在事件
     * @param evtResults
     * @param evtName
     * @return
     */
    private boolean existEvt(List<Map<String, Object>> evtResults,String evtName){
        for(Map<String, Object> item:evtResults){
            if(evtName.equalsIgnoreCase(item.get("evtName").toString())){
                return true;
            }
        }
        return false;
    }


    private class Ss {
        private Map<String, Double> thresholdMap;
        private List<int[]> quesRaods;
        private List<Map<String, Object>> slideWindow;
        private int start;
        private int end;
        private boolean flag;
        private int i;
        private Double rate;

        public Ss(Map<String, Double> thresholdMap, List<int[]> quesRaods, List<Map<String, Object>> slideWindow, int start, int end, boolean flag, int i, Double rate) {
            this.thresholdMap = thresholdMap;
            this.quesRaods = quesRaods;
            this.slideWindow = slideWindow;
            this.start = start;
            this.end = end;
            this.flag = flag;
            this.i = i;
            this.rate = rate;
        }

        public int getStart() {
            return start;
        }

        public int getEnd() {
            return end;
        }

        public boolean isFlag() {
            return flag;
        }
        public Ss invoke(String thresholdName) {
            if(rate*100>thresholdMap.get(thresholdName)){
                if(flag){
                    start=i;
                }
                flag=false;
            }else if(rate*100<=thresholdMap.get(thresholdName)-20){
                if(!flag){
                    quesRaods.add(new int[]{start,end-1});
                    slideWindow.clear();
                    start=i;
                    end=i;
                    flag=true;
                }

            }
            return this;
        }
    }
}