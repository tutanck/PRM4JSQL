/** WHAT THE FUCK */

function send_ajax_request_(HTTPMethod,url,request,dataType,response_processor,error_manager){
	$.ajax({
		type : HTTPMethod,
		url : url,
		data : request,
		dataType : "json",//be careful : can mask error real type otherwise use text
		success : response_processor,
		error : function(XHR, testStatus, errorThrown) {
			console.log("Unable to talk with the server at '"+url+"' " +
					"about '"+request+"' with '"+HTTPMethod+"' method " +
					"in '"+dataType+"' dialect");
			console.log("\n"+JSON.stringify(XHR) + " - " + testStatus + " - " + errorThrown); 
			if(error_manager!=undefined) error_manager(); 
		}
	});
}

function value_casting(value){
	if(value.length != 0 
			&& value!=undefined )
		return true;
	return false;
}

function value_number_casting(value){
	if(value.length != 0 
			&& value!=undefined 
			&& isNumber(value))
		return true;
	return false;
}

function walk_through(json,processor,recurse) {
	for (var key in json) 
		if (json.hasOwnProperty(key)) {
			var val=json[key];
			processor(key,val);
			if(recurse)
				flatten_json_object(val); 
		}
}

function printHTML(dom,htm){$(dom).html(htm); }

function emptyDiv(dom){$(dom).empty(); }

function printHTMLSup(dom,htm){	$(dom).append(htm); }

function printHTMLPre(dom,htm){	$(dom).prepend(htm); }

function printHTMLAfter(dom,htm){ $(dom).after(htm); }

function printDivAfter(dom,val){
	$("#"+dom).after("<div id=\"smarttag"+dom+"\" class=\"warning-wrapper\">"
			+val+"</div>\n");}

function printHTMLBefore(dom,htm){ $(dom).before(htm); }

function removeHTML(dom,htm){ $(dom).remove(htm); }

function removeElt(dom){$(dom).remove(); }

function isNumber(s){ return ! isNaN (s-0); }

function fillNOTIFIER(msg){ printHTML("#notifier",msg); }

function resetNOTIFIER(){ printHTML("#notifier",""); }

function notice(msg){
	fillNOTIFIER(msg);
	setTimeout(
			function(){
				resetNOTIFIER();   	        
			},3000);
}

function resetElt(dom){ printHTML(dom,""); }

function gotoURL(url){window.location.href=url};
/**
 * assign(url): Load the document at the provided URL.
 * replace(url):Replace the current document with the one at the provided URL.
 	The difference from the assign() method is that after using replace() 
 	the current page will not be saved in session history, 
 	meaning the user won't be able to use the Back button to navigate to it. */
function replaceURL(url){window.location.replace=url};

//redirige vers la page d'url 'location '
function redirect(location) { window.location.href = location;}

//Create a prefilled form completed with provided inputs and submit it
function CreateAndSubmitInnerForm(containerID,formID,method,action,formContent){
	var errDiv = document.getElementById(containerID)
	.innerHTML='<form id="'+formID+'" method="'+method+'" action="'+action+'">'
	+formContent+'</form>';
	var f = document.getElementById(formID);
	if(f) f.submit();
}

//attache le nom de l'ue a son groupe [Dlp,1]-> Dlp-1
function bind(tab){ return tab.join("-"); }

//attache le nom de l'ue a son groupe [Dlp,1]-> Dlp1
function bond(tab){return tab.join(""); }

//attache les noms des ues a leurs groupes respectifs [[Dlp,1],[IL,2]]->(Dlp-1,IL-2)
function bindAll(list){
	//alert("oldlist="+JSON.stringify(list));
	var newlist=[];
	for(var i=0;i<list.length;i++)
		newlist.push(bind(list[i]));
	//alert("newlist="+JSON.stringify(newlist));
	return newlist;
}

//attache les noms des ues a leurs groupes respectifs [[Dlp,1],[IL,2]]->(Dlp1,IL2)
function bondAll(list){ 
	//alert("oldlist="+JSON.stringify(list));
	var newlist=[];
	for(var i=0;i<list.length;i++)
		newlist.push(bond(list[i]));
	//alert("newlist="+JSON.stringify(newlist));
	return newlist;
}


//detache le nom de l'ue de son groupe Dlp-1->[Dlp,1]
function unbind(str){ return str.split("-"); }

//detache les noms des ues de leurs groupes respectifs (Dlp-1,IL-2)->[[Dlp,1],[IL,2]]
function unbindAll(list){  
	//alert("oldlist="+JSON.stringify(list));
	var newlist=[];
	for(var i=0;i<list.length;i++)
		newlist.push(unbind(list[i]));
	//alert("newlist="+JSON.stringify(newlist));
	return newlist;
}

//print in consol map content unfold means [derouler, deplier]
function unfold(map){
	for (var [key,val] of map) 
		  console.log(key + " = " + JSON.stringify(val));
}



/**
 * GOOGLE MAP API GENERIZER*/

function functionalize_curent_position(map,markertitle,zoom,func){
	//alert('functionalize_curent_position : '+map+","+markertitle+","+zoom);
	var options = {
			enableHighAccuracy: true,
			timeout: 60000,//TODO what if user is slow to allow get_postion
			maximumAge: 0
	};

	function success(pos) {
		var crd = pos.coords;
		console.log("Your current position is:" +
				"(" + crd.latitude+","+ crd.longitude+","+ crd.accuracy + "m)\n");
		var marker=add_gmap_marker(
				map,crd.latitude,crd.longitude,markertitle);
		marker.setIcon('http://www.geocodezip.com/mapIcons/marker_shadow.png');
		map.setCenter(marker.getPosition()); //Centralize map on user position
		set_marker_autofocusable(map,marker,zoom); //marker remains always autofocusable
		set_marker_functional_on_position(map,marker,func,crd.latitude,crd.longitude);
	};

	function error(err) {
		console.warn('ERROR(' + err.code + '): ' + err.message);
	};

	if (navigator.geolocation) 
		navigator.geolocation.getCurrentPosition(success, error, options);
	else return console.log("Geolocation is not supported by this browser."); 		
}


function newMap(dom,lat,lon,zoom/*,type/*=google.maps.MapTypeId.ROADMAP*/) {
	return new google.maps.Map(document.getElementById(dom), {
		center: {lat: lat, lng: lon},
		zoom: zoom
		//,mapTypeId:type
	});
}

function add_gmap_marker(map,lat,lon,title){
	//alert("add_gmap_marker : "+map+","+lat+","+lon+","+title);
	return new google.maps.Marker({
		position: {lat : lat,lng : lon}, 
		map: map,
		title: title
	});
}

function set_marker_autofocusable(map,marker,zoom){
	//alert("set_marker_autofocusable : "+map+","+marker+","+zoom);
	marker.addListener('click', function() {
		map.setZoom(zoom);
		map.setCenter(marker.getPosition());
		//map.panTo(marker.getPosition());
	});
}

function set_marker_functional_on_position(map,marker,func,lat,lon,target){
	//alert("set_marker_functional_on_position : "+map+","+marker+","+func+","+lat+","+lon);
	google.maps.event.addListener(marker, 'click', function(event) {
		map.setZoom(18);
		map.setCenter(marker.getPosition());
		func(event,map,marker,lat,lon,target);
	});
}

function focus_on_marker(map,marker,zoom){
	//alert("focus_on_marker : "+map+","+marker+","+zoom);
	map.setZoom(zoom);
	map.setCenter(marker.getPosition());
	//map.panTo(marker.getPosition());
}

function set_map_autofocusable_on_marker(map,marker,timeout){
	map.addListener('center_changed', function() {
		// 3 seconds after the center of the map has changed, pan back to the
		// marker.
		window.setTimeout(function() {
			map.panTo(marker.getPosition());
		}, timeout);
	});
}

/**
 * GOOGLE MAP API GENERIZER****************************************************/

function sendserver_curent_position_context(radius,rows,func){
	//alert('sendserver_curent_position_context : '+map+","+markertitle+","+zoom);
	var options = {
			enableHighAccuracy: true,
			timeout: 60000,//TODO what if user is slow to allow get_postion
			maximumAge: 0
	};

	function success(pos) {
		var crd = pos.coords;
		console.log("Sending local context to server from current position:" +
				"(" + crd.latitude+","+ crd.longitude+","+ crd.accuracy + "m)\n");
		 
		send_ajax_request_("POST","api","lat="+crd.latitude+"&lon="+crd.longitude
				+"&rad="+radius+"&row="+rows,"json",ajax_reply_processor_,func);
	};

	function error(err) {
		console.warn('ERROR(' + err.code + '): ' + err.message);
	};

	if (navigator.geolocation) 
		navigator.geolocation.getCurrentPosition(success, error, options);
	else return console.log("Geolocation is not supported by this browser."); 		
}



