function ajax_reply_processor_(rep){
	//alert("ajax_reply_processor_ ->"+JSON.stringify(rep));
	if(rep.iserror!=undefined)
		return process_error(rep);
	//alert("ajax_reply_processor_ ->status = "+rep.status+" -> rpcode = "+rep.rpcode);
	if (rep.status==-1) //bad
		process_message(rep.message,rep.rpcode);
	else if (rep.status==0)//good 
		process_result(rep.result,rep.rpcode);
	else if (rep.status==1) //both
		process_reply(rep.result,rep.message,rep.rpcode);
}

function process_message(message,rpcode){ 
	if(rpcode==-32)
		gotoURL("/");//TODO make it generic by page redirect af msg display : see process reply & process_error 
	if(message!=undefined)
		fillNOTIFIER(message);
}

function process_result(result,rpcode){
	//alert("process_result ->result = "+JSON.stringify(result)+" ->rpcode = "+rpcode);
	(processor(rpcode))(result);	
}


function process_reply(result,message,rpcode){
	process_message(message, rpcode); //display message then
	//process the response 
	setTimeout(
			function(){
				process_result(result, rpcode);   	        
			},3000);
}

function process_error(error){
	//console.log("Got an error from server : \n"+error.iserror); //TODO ask why il trouve pas
	/* TODO CreateAndSubmitInnerForm("errFormContainer","sendErr","post","err.jsp",
			'<input name="e" type="hidden" value="ok"/>');*/
		gotoURL(error.errorpage+"?e="+error.iserror);
		//http://stackoverflow.com/questions/1735230/can-i-add-custom-attribute-to-html-tag
}

function Gate(fields,form,url,dataType,response_processor){
	//alert('Gate:');
	if(KFC(fields))
		connect(fields,form,url,dataType,response_processor);	
}

/**
 * -Stringify the form's fields into an url's request
 * -call the send_ajax_request with provided parameters and home made url's request
 * @param fields
 * @param form
 * @param url
 * @param dataType
 * @param response_processor */
function connect(fields,form,url,dataType,response_processor){
	var request="";
	for(var i=0; i<fields.length;i++)
		if(i==fields.length-1)
			request+=fields[i].name+"="+fields[i].value;
		else
			request+=fields[i].name+"="+fields[i].value+"&";
	//alert("connect->"+request);
	send_ajax_request_(form.method, url, request, dataType, response_processor)
}

/**
 * KeybasedFormChecking
 * @param fields
 * @returns {Boolean}
 */
function KFC(fields){
	//alert('kfc');
	approval=true;
	for(var i in fields)
		if(fields[i].value.trim().length==0){
			approval=false;
			if(!document.getElementById("smarttag"+fields[i].id))
				printDivAfter(fields[i].id,"<center><font color='red'>Le champ "+fields[i].name +" est obligatoire.</font></center>");
			else
				printHTML("#smarttag"+fields[i].id,"<center><font color='red'>Le champ "+fields[i].name +" est obligatoire.</font></center>");
		}else 
			if(document.getElementById("smarttag"+fields[i].id))
				printHTML("#smarttag"+fields[i].id,"");
	
	/**less generic - but still generic : */
	if(fields[i].hasAttribute("twin")) 
		alert("yo");
	//TODO get twins and check they have same value
	
	
	
	return approval;
}