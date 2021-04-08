//>>built
define("dojox/data/FlickrRestStore","dojo/_base/lang dojo/_base/declare dojo/_base/array dojo/io/script dojox/data/FlickrStore dojo/_base/connect".split(" "),function(p,u,q,v,s,w){var t=u("dojox.data.FlickrRestStore",s,{constructor:function(a){a&&(a.label&&(this.label=a.label),a.apikey&&(this._apikey=a.apikey));this._cache=[];this._prevRequests={};this._handlers={};this._prevRequestRanges=[];this._maxPhotosPerUser={};this._id=t.prototype._id++},_id:0,_requestCount:0,_flickrRestUrl:"http://www.flickr.com/services/rest/",
_apikey:null,_storeRef:"_S",_cache:null,_prevRequests:null,_handlers:null,_sortAttributes:{"date-posted":!0,"date-taken":!0,interestingness:!0},_fetchItems:function(a,d,l){var b={};a.query?p.mixin(b,a.query):a.query=b={};var e=[],f=[],c={format:"json",method:"flickr.photos.search",api_key:this._apikey,extras:"owner_name,date_upload,date_taken,geo"};b.userid&&(c.user_id=a.query.userid,e.push("userid"+a.query.userid));b.groupid&&(c.group_id=b.groupid,e.push("groupid"+b.groupid));if(b.apikey)c.api_key=
a.query.apikey,f.push("api"+a.query.apikey);else if(c.api_key)a.query.apikey=c.api_key,f.push("api"+c.api_key);else throw Error("dojox.data.FlickrRestStore: An API key must be specified.");a._curCount=a.count;if(b.page)c.page=a.query.page,f.push("page"+c.page);else if("start"in a&&null!==a.start){a.count||(a.count=20);var g=a.start,h=a.count;if(0!==a.start%a.count){if(g<h/2)h=g+h,g=0;else{for(var m=2,n=20;0<n;n--)if(0===g%n&&g/n>=h){m=n;break}h=g/m}a._realStart=a.start;a._realCount=a.count;a._curStart=
g;a._curCount=h}else a._realStart=a._realCount=null,a._curStart=a.start,a._curCount=a.count;c.page=g/h+1;f.push("page"+c.page)}a._curCount&&(c.per_page=a._curCount,f.push("count"+a._curCount));b.lang&&(c.lang=a.query.lang,e.push("lang"+a.lang));b.setid&&(c.method="flickr.photosets.getPhotos",c.photoset_id=a.query.setid,e.push("set"+a.query.setid));if(b.tags&&(c.tags=b.tags instanceof Array?b.tags.join(","):b.tags,e.push("tags"+c.tags),b.tag_mode&&("any"===b.tag_mode.toLowerCase()||"all"===b.tag_mode.toLowerCase())))c.tag_mode=
b.tag_mode;b.text&&(c.text=b.text,e.push("text:"+b.text));b.bbox&&(c.bbox=b.bbox,e.push("bbox:"+b.bbox));b.sort&&0<b.sort.length?(b.sort[0].attribute||(b.sort[0].attribute="date-posted"),this._sortAttributes[b.sort[0].attribute]&&(c.sort=b.sort[0].descending?b.sort[0].attribute+"-desc":b.sort[0].attribute+"-asc")):c.sort="date-posted-asc";e.push("sort:"+c.sort);var e=e.join("."),f=0<f.length?"."+f.join("."):"",k=e+f;a={query:b,count:a._curCount,start:a._curStart,_realCount:a._realCount,_realStart:a._realStart,
onBegin:a.onBegin,onComplete:a.onComplete,onItem:a.onItem};d={request:a,fetchHandler:d,errorHandler:l};if(this._handlers[k])this._handlers[k].push(d);else{this._handlers[k]=[d];var b={url:this._flickrRestUrl,preventCache:this.urlPreventCache,content:c,callbackParamName:"jsoncallback"},r=p.hitch(this,function(a,b,c){var f=c.request.onBegin;c.request.onBegin=null;var d=c.request;"_realStart"in d&&null!=d._realStart&&(d.start=d._realStart,d.count=d._realCount,d._realStart=d._realCount=null);f&&(d=null,
b&&(d=b.photoset?b.photoset:b.photos),d&&"perpage"in d&&"pages"in d?(b=d.perpage*d.pages<=c.request.start+c.request.count?c.request.start+d.photo.length:d.perpage*d.pages,this._maxPhotosPerUser[e]=b,f(b,c.request)):this._maxPhotosPerUser[e]&&f(this._maxPhotosPerUser[e],c.request));c.fetchHandler(a,c.request);f&&(c.request.onBegin=f)}),c=p.hitch(this,function(b){if("ok"!=b.stat)l(null,a);else{var c=this._handlers[k];if(c){this._handlers[k]=null;this._prevRequests[k]=b;var d=this._processFlickrData(b,
a,e);this._prevRequestRanges[e]||(this._prevRequestRanges[e]=[]);this._prevRequestRanges[e].push({start:a.start,end:a.start+(b.photoset?b.photoset.photo.length:b.photos.photo.length)});q.forEach(c,function(a){r(d,b,a)})}else console.log("FlickrRestStore: no handlers for data",b)}});(f=this._prevRequests[k])?(this._handlers[k]=null,r(this._cache[e],f,d)):this._checkPrevRanges(e,a.start,a.count)?(this._handlers[k]=null,r(this._cache[e],null,d)):(d=v.get(b),d.addCallback(c),d.addErrback(function(b){w.disconnect(null);
l(b,a)}))}},getAttributes:function(a){return"title author imageUrl imageUrlSmall imageUrlMedium imageUrlThumb imageUrlLarge imageUrlOriginal link dateTaken datePublished".split(" ")},getValues:function(a,d){this._assertIsItem(a);this._assertIsAttribute(d);switch(d){case "title":return[this._unescapeHtml(a.title)];case "author":return[a.ownername];case "imageUrlSmall":return[a.media.s];case "imageUrl":return[a.media.l];case "imageUrlOriginal":return[a.media.o];case "imageUrlLarge":return[a.media.l];
case "imageUrlMedium":return[a.media.m];case "imageUrlThumb":return[a.media.t];case "link":return["http://www.flickr.com/photos/"+a.owner+"/"+a.id];case "dateTaken":return[a.datetaken];case "datePublished":return[a.datepublished]}},_processFlickrData:function(a,d,l){if(a.items)return s.prototype._processFlickrData.apply(this,arguments);var b=["http://farm",null,".static.flickr.com/",null,"/",null,"_",null],e=[],f=a.photoset?a.photoset:a.photos;if("ok"==a.stat&&f&&f.photo){e=f.photo;for(f=0;f<e.length;f++){var c=
e[f];c[this._storeRef]=this;b[1]=c.farm;b[3]=c.server;b[5]=c.id;b[7]=c.secret;var g=b.join("");c.media={s:g+"_s.jpg",m:g+"_m.jpg",l:g+".jpg",t:g+"_t.jpg",o:g+"_o.jpg"};!c.owner&&a.photoset&&(c.owner=a.photoset.owner)}}var h=d.start?d.start:0,m=this._cache[l];m||(this._cache[l]=m=[]);q.forEach(e,function(a,b){m[b+h]=a});return m},_checkPrevRanges:function(a,d,l){var b=d+l;a=this._prevRequestRanges[a];return!!a&&q.some(a,function(a){return d>=a.start&&b<=a.end})}});return t});