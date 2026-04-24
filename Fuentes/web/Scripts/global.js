
function initialize(str)
{
  try
  {
    top.frames["topFrame"].navegacion.innerHTML=str;
  }
  catch(err)
  {
    document.title = str;
  }
  return true;
}


function msg(str)
{
  window.status = str;
  return true;
}

function getPath()
{
  if(location.protocol == "http:")
    return location.protocol + "//" + location.hostname + location.pathname.substring(1,location.pathname.lastIndexOf("/"));
  else
    return location.pathname.substring(1,location.pathname.lastIndexOf("\\")) + "\\";
}

function getRootPath()
{
  return top.frames["topFrame"].frmEntorno.rootPath.value;
}

function Calendar()
{
 return window.showModalDialog(getRootPath() + 'scripts/calendar.htm','', 'dialogHeight:140px;dialogWidth:140px;center:yes;status:no;scroll:no');
}

function MM_swapImgRestore() { // v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_preloadImages() { // v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_findObj(n, d) { // v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_swapImage() { // v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}

function MM_reloadPage(init) {  // reloads the window if Nav4 resized
  if (init==true) with (navigator) {if ((appName=="Netscape")&&(parseInt(appVersion)==4)) {
    document.MM_pgW=innerWidth; document.MM_pgH=innerHeight; onresize=MM_reloadPage; }}
  else if (innerWidth!=document.MM_pgW || innerHeight!=document.MM_pgH) location.reload();
}
MM_reloadPage(true);

function MM_showHideLayers() { // v6.0
  var i,j,p,v,obj,args=MM_showHideLayers.arguments;
  for (i=0; i<(args.length-2); i+=3)
  	if ((obj=MM_findObj(args[i]))!=null)
  	{ 	v=args[i+2];
    		if (obj.style)
    		{
    			if (args[3]!=null) // Marcos: Ponemos el texto que pasamos como
									// argumento
    				if (args[3].length > 0)
    				{
    					obj.value = args[3];
    					for (j=args[3].length; j<200; j++)
    					// Marcos: Rellenamos de espacios por la derecha, para
						// que no aparezcan lineas cortadas
    						obj.value = obj.value + " ";
    				}
    			obj=obj.style; v=(v=='show')?'visible':(v=='hide')?'hidden':v;
    		}

    		obj.visibility=v;
    	}
}

function MM_callJS(jsStr) { // v2.0
  return eval(jsStr);
}

// Centra ventana en pantalla
function posLeft(ancho){
	var posicion = parseInt((screenWidth() - ancho)/2);
	return posicion;
}

function posTop(alto){
	var posicion = parseInt((screenHeight() - alto)/2);
	return posicion;
}

function screenWidth() {
	return window.screen.width;
}

function screenHeight() {
	return window.screen.height;
}

function windowParams(ancho, alto) {
	if (ancho == null) ancho = parseInt(screenWidth()/2);
	if (alto == null) alto = parseInt(screenHeight()/2);
	var params = "menubar=no,statusbar=no,toolbar=no,personalbar=no,scrollbars=no,left=" + posLeft(ancho) + ",top=" + posTop(alto) + ",width=" + ancho + ",height=" + alto;
	return params;
}

function getDataAgora() {
  var dataAgora = new Date();
  var dd = ("00" + dataAgora.getDate()).substring(("" + dataAgora.getDate()).length);
  var mm = ("00" + (dataAgora.getMonth()+1)).substring(("" + (dataAgora.getMonth()+1)).length);
  var yyyy = dataAgora.getFullYear();
  return  dd + "/" +  mm + "/" + yyyy;
}

function getHoraAgora() {
  var dataAgora = new Date();
  var hh = ("00" + dataAgora.getHours()).substring(("" + dataAgora.getHours()).length);
  var mm = ("00" + dataAgora.getMinutes()).substring(("" + dataAgora.getMinutes()).length);
  return hh + ":" + mm;
}

// Trata las lineas de los listados cambiando de color al hacer un onmouseover sobre ellas
var linSel = -1;

function resaltar(linea) {
	if (linea != linSel) {
		document.getElementById('TR'+linea).className='colorLineaResaltada';
	}
}

function restaurar(linea) {
	if ((linea != linSel) && (linea != -1)) {
		if (1*linea%2 == 0)
			document.getElementById('TR'+linea).className='colorLineaPar';
		else
			document.getElementById('TR'+linea).className='colorLineaImpar';
	}
}

/*
 * Recupera de los parametros pasados por GET el valor correspondiente a la
 * clave pasada como parametro
 */
function getUrlValue(varSearch) {
	var searchString = window.location.search.substring(1);
	var variableArray = searchString.split('&');
	for (var i = 0; i < variableArray.length; i++) {
		var keyValuePair = variableArray[i].split('=');
		if (keyValuePair[0] == varSearch) {
			return keyValuePair[1];
		}
	}
}
