// All material copyright ESRI, All Rights Reserved, unless otherwise specified.
// See http://js.arcgis.com/3.16/esri/copyright.txt for details.
//>>built
define("esri/dijit/geoenrichment/InfographicsCarousel","../../declare dojo/_base/lang dojo/when ./InfographicsCarouselBase ./InfographicsFactory ../../tasks/geoenrichment/GeometryStudyArea".split(" "),function(c,b,d,e,f,g){return c("esri.dijit.geoenrichment.InfographicsCarousel",e,{studyAreaTitle:null,_getCountryPromise:null,_countryForStudyArea:!1,startup:function(){this._started||(this.infographicsFactory=this.infographicsFactory||new f,this.inherited(arguments))},_setStudyAreaTitleAttr:function(a){this._set("studyAreaTitle",
a);this._updateSubtitle()},_setStudyAreaAttr:function(a){this._set("studyArea",a);this._started&&a&&(this._countryForStudyArea=!1,this._getCountryPromise||(this._infographic.get("countryID")?this._infographic.set("studyArea",a):this._getCountry()),this._updateSubtitle())},_getCountry:function(){var a=this.get("studyArea");this._getCountryPromise=this.infographicsFactory.getCountry(a);d(this._getCountryPromise,b.hitch(this,this._onGetCountryComplete,a),b.hitch(this,this._onDataError))},_onGetCountryComplete:function(a,
b){this._getCountryPromise=null;this.studyArea===a&&(this._countryForStudyArea=!0);this._infographic.set("countryID",b);this._infographic.set("studyArea",this.studyArea);this._getReports()},_getReports:function(){this._getCountryPromise||this.inherited(arguments)},_updateSubtitle:function(){this.set("subtitle",this.studyArea instanceof g?"polygon"==this.studyArea.geometry.type?this.studyAreaTitle?this.studyAreaTitle:"${name}":this.studyAreaTitle?"\x3cdiv\x3e${address}\x3c/div\x3e\x3cdiv\x3e"+this.studyAreaTitle+
" (${name})\x3c/div\x3e":"\x3cdiv\x3e${address}\x3c/div\x3e\x3cdiv\x3e${name}\x3c/div\x3e":"\x3cdiv\x3e${address}\x3c/div\x3e\x3cdiv\x3e${name}\x3c/div\x3e")},_onDataReady:function(a){!this.inherited(arguments)&&!this._countryForStudyArea&&(this._infographic.set("variables",null),a.stop&&(this._infographic.setGeoenrichment(this.infographicsFactory.createGeoenrichment()),a.stop(),this._getCountry()))}})});