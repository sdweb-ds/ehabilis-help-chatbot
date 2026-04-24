// JavaScript Document
//
/*Poner esta linea en el documento que utilice estas funciones:
  <script language="JavaScript" src="./scripts/libreriaCapas.js"></script>
*/
/* INTERFACES:

//Hace visible una capa
function muestraCapa( capa ){

//Hace invisible una capa
function ocultaCapa( capa ){

//Posiciona una capa en unas coordenadas dadas y la hace visible
function posicionaCapa( capa, x, y ){

//Registra los datos del clip de una capa y lo aplica
function registraClip( capa, capaleft, capatop, caparight, capabottom ){

//Muestra una zona concreta de la capa
function poneClip( capa, capaleft, capatop, caparight, capabottom ){

//Desplaza una capa unos incrementos dados
function desplazaCapa( capa, dx, dy ){

//Modifica el ancho y el alto de la capa
function dimensionaCapa( capa, ancho, alto ){

//Modifica el ancho y el alto de la capa un diferencial
function redimensionaCapa( capa, dancho, dalto ){

//Oculta varias capas cuyos nombres vienen separados por comas
function ocultaCapas( capas ){

//Muestra varias capas cuyos nombres vienen separados por comas
function muestraCapas( capas ){

//Desplaza una capa en los dos ejes, a una velocidad y un desplazamiento indicados
function animaCapaXY( capa, dx, dy, velocidad ){

//Anima un lado del clip (0,1,2,3) un desplazamiento indicado a una velocidad dada
function animaCapaXYClip( capa, borde, desp, velocidad ){

//Incluir esta funci?n en onMouseMove del body para tener los datos del rat?n
function obtenXYRaton(){

//Inicia el arrastre de la capa (onMouseDown)*
function arrastraCapa(capa){

//Finaliza el arrastre de la capa  (onMouseUp)*
function sueltaCapa(capa){

////////////////////////////
//Funciones EXPERIMENTALES//
////////////////////////////
//Expande/encoge una capa en los dos ejes, a una velocidad y un desplazamiento indicados
function animaCapaWH( capa, dw, dh, velocidad ){

*/

//Variables globales necesarias

//Almacenes temporales de capas
var ccapa = new Array(null,null,null,null,null,null,null,null,null,null); //Capa

var cleft = new Array(0,0,0,0,0,0,0,0,0,0); //Coordenadas del clip de la capa especificada
var ctop = new Array(0,0,0,0,0,0,0,0,0,0);
var cright = new Array(0,0,0,0,0,0,0,0,0,0);
var cbottom = new Array(0,0,0,0,0,0,0,0,0,0);

var cestado = new Array(0,0,0,0,0,0,0,0,0,0); //Estado del clip 0=Est?tico, 1=En movimiento
var mestado = new Array(0,0,0,0,0,0,0,0,0,0); //Estado del movimiento de una capa 0=Est?tico, 1= En movimiento
var destado = new Array(0,0,0,0,0,0,0,0,0,0); //Estado del dimensionado de una capa 0=Est?tico, 1= Redimension?ndose

//Coordenadas del rat?n
var x_mouse, y_mouse;
//Almacena datos de la capa que est? siendo arrastrada
//0,1 - Arrastrando capa
//Nombre de la capa
//dx dentro de la capa del puntero
//dy dentro de la capa del puntero
var mouse_arrastre = new Array( 0, null, 0, 0 );

//Funciones generales
//Incluir esta funci?n en onMouseMove del body para tener los datos del rat?n
function obtenXYRaton(){
	x_mouse = window.event.x;
	y_mouse = window.event.y;
	//Arrastramos la capa, si procede
	if( mouse_arrastre[0] == 1 ){ 
		posicionaCapa( mouse_arrastre[1], x_mouse - mouse_arrastre[2], y_mouse - mouse_arrastre[3] );
	}
}


//Hace visible una capa
function muestraCapa( capa ){
	document.getElementById(capa).style.visibility="visible";
}

//Hace invisible una capa
function ocultaCapa( capa ){
	document.getElementById(capa).style.visibility="hidden";
}

//Devuelve las coordenadas, ancho y alto de una capa
function obtX( capa ){
	switch( document.getElementById(capa).style.position ){
		case "absolute":
				return parseInt(document.getElementById(capa).style.left);
			break;
		case "relative":
				return document.getElementById(capa).offsetLeft;
			break;
	}
}
function obtY( capa ){
	switch( document.getElementById(capa).style.position ){
		case "absolute":
				return parseInt(document.getElementById(capa).style.top);
			break;
		case "relative":
				return document.getElementById(capa).offsetTop;
			break;
	}
}
function ancho( capa ){
	switch( document.getElementById(capa).style.position ){
		case "absolute":
				return parseInt(document.getElementById(capa).style.width);
			break;
		case "relative":
				return document.getElementById(capa).offsetWidth;
			break;
	}
}
function alto( capa ){
	switch( document.getElementById(capa).style.position ){
		case "absolute":
				return parseInt(document.getElementById(capa).style.height);
			break;
		case "relative":
				return document.getElementById(capa).offsetHeight;
			break;
	}
}

//Posiciona una capa en unas coordenadas dadas y la hace visible
function posicionaCapa( capa, x, y ){
	switch( document.getElementById(capa).style.position ){
		case "absolute":
			document.getElementById(capa).style.left= x;
			document.getElementById(capa).style.top= y;
			break;
		case "relative":
			document.getElementById(capa).style.left = x - document.getElementById(capa).offsetLeft;
			document.getElementById(capa).style.top = y - document.getElementById(capa).offsetTop;
			
			break;
	}
	document.getElementById(capa).style.visibility="visible";
}

//Registra los datos del clip de una capa ABSOLUTA y lo aplica
function registraClip( capa, capaleft, capatop, caparight, capabottom ){
	var num_clip = 0;
	//Buscamos un hueco libre en la zona temporal de clips
	while( (num_clip < ccapa.length) && (ccapa[num_clip] != capa) && (ccapa[num_clip] != null) ) num_clip++;
	if( num_clip < ccapa.length ){
		//En caso de omitir ciertos datos, los inferimos ( 0, 0, w, h )
		if( capaleft + "." == "undefined." ) capaleft = 0;
		if( capatop + "." == "undefined." ) capatop = 0;
		if( caparight + "." == "undefined." ) caparight = parseInt(document.all.item(capa).style.width);
		if( capabottom + "." == "undefined." ) capabottom = parseInt(document.all.item(capa).style.height);
		//Si es posible, almacenamos los datos del clip para usos posteriores
		ccapa[num_clip] = capa;
  	    cleft[num_clip] = capaleft; 
		ctop[num_clip] = capatop; 
		cright[num_clip] = caparight; 
		cbottom[num_clip] = capabottom;
	}
	poneClip( capa, capaleft, capatop, caparight, capabottom );
}

//Muestra una zona concreta de la capa ABSOULTA
function poneClip( capa, capaleft, capatop, caparight, capabottom ){
	document.all.item(capa).style.clip = "rect(" + capatop.toString() + "px, "
		    	                               + caparight.toString() + "px, " 
											   + capabottom.toString() + "px, " 
											   + capaleft.toString() + "px)";		 
}

//Desplaza una capa unos incrementos dados
function desplazaCapa( capa, dx, dy ){
	switch( document.all.item(capa).style.position ){
		case "absolute":
			x=parseInt(document.all.item(capa).style.left);
			y=parseInt(document.all.item(capa).style.top);
			break;
		case "relative":
			x=document.all.item(capa).offsetLeft;
			y=document.all.item(capa).offsetTop;
			break;
	}
	posicionaCapa( capa, x + dx, y + dy );
}

//Modifica el ancho y el alto de la capa
function dimensionaCapa( capa, ancho, alto ){
	/*switch( document.all.item(capa).style.position ){
		case "absolute":
			document.all.item(capa).style.width = ancho;
			document.all.item(capa).style.height = alto;
			break;
		case "relative":
		//Depende de otra capa, no es posible
			break;
	}*/
	switch( document.getElementById(capa).style.position ){
		case "absolute":
			document.getElementById(capa).style.width = ancho;
			document.getElementById(capa).style.height = alto;
			break;
		case "relative":
			break;
	}

//	document.all.item(capa).style.width = ancho;
//	document.all.item(capa).style.height=alto;
}

//Modifica el ancho y el alto de la capa un diferencial
function redimensionaCapa( capa, dancho, dalto ){
	switch( document.all.item(capa).style.position ){
		case "absolute":
			x=parseInt(document.all.item(capa).style.width);
			y=parseInt(document.all.item(capa).style.height);
			break;
		case "relative":
			x=document.all.item(capa).offsetWidth;
			y=document.all.item(capa).offsetHeight;
			break;
	}
	dimensionaCapa( capa, x + dancho, y + dalto );
}

//Oculta varias capas cuyos nombres vienen separados por comas
function ocultaCapas( capas ){
	var acapas = capas.split(",");
	for( i=0; i<acapas.length; i++ ) ocultaCapa( acapas[i] );
}

//Muestra varias capas cuyos nombres vienen separados por comas
function muestraCapas( capas ){
	var acapas = capas.split(",");
	for( i=0; i<acapas.length; i++ ) muestraCapa( acapas[i] );
}

//Desplaza una capa de forma vertical/horizontal hasta alcanzar la y_ref/h_ref
function animaCapaXYMotor( capa, x_ref, y_ref, velocidad ){
	switch( document.all.item(capa).style.position ){
		case "absolute":
			x=parseInt(document.all.item(capa).style.left);
			y=parseInt(document.all.item(capa).style.top);
			break;
		case "relative":
			x=document.all.item(capa).offsetLeft;
			y=document.all.item(capa).offsetTop;
			break;
	}

	//Desplazamos el eje y
	if( y != y_ref )
	 if( y > (y_ref + velocidad)) desplazaCapa( capa, 0, -velocidad );
	 else if( y < (y_ref - velocidad) ) desplazaCapa( capa, 0, velocidad );
	 else posicionaCapa( capa, x, y_ref );

	switch( document.all.item(capa).style.position ){
		case "absolute":
			y=parseInt(document.all.item(capa).style.top);
			break;
		case "relative":
			y=document.all.item(capa).offsetTop;
			break;
	}

	//Desplazamos el eje x
	if( x != x_ref )
	 if( x > (x_ref + velocidad)) desplazaCapa( capa, -velocidad, 0 );
	 else if( x < (x_ref - velocidad) ) desplazaCapa( capa, velocidad, 0 );
	 else posicionaCapa( capa, x_ref, y );
	switch( document.all.item(capa).style.position ){
		case "absolute":
			x=parseInt(document.all.item(capa).style.left);
			break;
		case "relative":
			x=parseInt(document.all.item(capa).offsetLeft);
			break;
	}

	if( (x != x_ref) || (y != y_ref) ){
		var param = new Array( "'"+capa+"'", x_ref, y_ref, velocidad );
		setTimeout( "animaCapaXYMotor("+param.join(",")+")", 20 );
	}else{
		//La capa ha finalizado su movimiento
		var i = 0;
		//Buscamos la capa
		while( ccapa[i] != capa ) i++;
		mestado[i] = 0; //La capa ahora est? en reposo
	}
}

//Desplaza una capa en los dos ejes, a una velocidad y un desplazamiento indicados
// capa = capa a animar
// tempo = temporizador a utilizar
// dx = desplazamiento en el eje x
// dy = desplazamiento en el eje y
// velocidad = diferencial de salto
function animaCapaXY( capa, dx, dy, velocidad ){
	var i = 0;
	//Buscamos un hueco libre para la capa
	while( (i < ccapa.length) && (ccapa[i] != capa) && (ccapa[i] != null) ) i++;
	if(( i < ccapa.length )	&& (mestado[i] == 0)){ //Si la capa est? ya en movimiento, no dejamos el mismo
		ccapa[i] = capa; //Asignamos la capa
		mestado[i] = 1; //La capa est? en movimiento
	    //Movemos la capa si no est? siendo animada previamente
		switch( document.all.item(capa).style.position ){
			case "absolute":
				x=parseInt(document.all.item(capa).style.left);
				y=parseInt(document.all.item(capa).style.top);
				break;
			case "relative":
				x=document.all.item(capa).offsetLeft;
				y=document.all.item(capa).offsetTop;
				break;
		}
		var param = new Array( "'"+capa+"'", x+dx, y+dy, velocidad );
		setTimeout( "animaCapaXYMotor("+param.join(",")+")", 20 );
	}else{
		//Impedimos el movimiento y avisamos del suceso
		return -1;
	}
}

//Redimensiona el clip
function animaClipMotor( capa, num_clip, l_ref, t_ref, r_ref, b_ref, velocidad ){

	if( cleft[num_clip] != l_ref )
	 if( cleft[num_clip] > (l_ref + velocidad) ) cleft[num_clip] -= velocidad;
	 else if( cleft[num_clip] < (l_ref - velocidad) ) cleft[num_clip] += velocidad;
	 else cleft[num_clip] = l_ref;

	if( ctop[num_clip] != t_ref )
  	 if( ctop[num_clip] > (t_ref + velocidad) ) ctop[num_clip] -= velocidad;
	 else if( ctop[num_clip] < (t_ref - velocidad) ) ctop[num_clip] += velocidad;
	 else ctop[num_clip] = t_ref;

	if( cright[num_clip] != r_ref )
	 if( cright[num_clip] > (r_ref + velocidad) ) cright[num_clip] -= velocidad;
	 else if( cright[num_clip] < (r_ref - velocidad) ) cright[num_clip] += velocidad;
	 else cright[num_clip] = r_ref;

	if( cbottom[num_clip] != b_ref )
	 if( cbottom[num_clip] > (b_ref + velocidad) ) cbottom[num_clip] -= velocidad;
	 else if( cbottom[num_clip] < (b_ref - velocidad) ) cbottom[num_clip] += velocidad;
	 else cbottom[num_clip] = b_ref;

    poneClip( capa, cleft[num_clip], ctop[num_clip], cright[num_clip], cbottom[num_clip] );
	
	if( (cleft[num_clip] != l_ref) || (ctop[num_clip] != t_ref) || (cright[num_clip] != r_ref) || (cbottom[num_clip] != b_ref) ){
			var param = new Array( "'"+capa+"'", num_clip, l_ref, t_ref, r_ref, b_ref, velocidad );
			setTimeout( "animaClipMotor("+param.join(",")+")", 20 );		
	}else{
		var i = 0;
		//Buscamos la capa
		while( ccapa[i] != capa ) i++;
		cestado[i] = 0; //La capa ha dejado de ser clipeada
	}
}

//Anima un lado del clip (0,1,2,3) un desplazamiento indicado a una velocidad dada
function animaCapaXYClip( capa, borde, desp, velocidad ){
	var param, num_clip, i;
	w=parseInt(document.all.item(capa).style.width);
	h=parseInt(document.all.item(capa).style.height);

	i = 0;
	//Buscamos un hueco libre
	while( (i < ccapa.length) && (ccapa[i] != capa) && (ccapa[i] != null) ) i++;
	if(( i < ccapa.length )	&& (cestado[i] == 0)){
		cestado[i] = 1; //La capa est? siendo clipeada
		ccapa[i] = capa;
		num_clip = i;
		switch( borde ){
			case 0: // Borde izdo
				param = new Array( "'"+capa+"'", num_clip, cleft[num_clip] + desp, ctop[num_clip], cright[num_clip], cbottom[num_clip], velocidad );
				break;
			case 1: // Borde superior
				param = new Array( "'"+capa+"'", num_clip, cleft[num_clip], ctop[num_clip] + desp, cright[num_clip], cbottom[num_clip], velocidad );
				break;
			case 2: // Borde dcho
				param = new Array( "'"+capa+"'", num_clip, cleft[num_clip], ctop[num_clip], cright[num_clip] + desp, cbottom[num_clip], velocidad );
				break;
			case 3: // Borde inferior
				param = new Array( "'"+capa+"'", num_clip, cleft[num_clip], ctop[num_clip], cright[num_clip], cbottom[num_clip] + desp, velocidad );
				break;
		}
		setTimeout( "animaClipMotor("+param.join(",")+")", 20 );
	}else{
		return -1; //Avisamos del suceso
	}
}


//Inicia el arrastre de la capa (onMouseDown)
function arrastraCapa(capa){
	switch( document.all.item(capa).style.position ){
		case "absolute":
			x=parseInt(document.all.item(capa).style.left);
			y=parseInt(document.all.item(capa).style.top);
			break;
		case "relative":
			x=document.all.item(capa).offsetLeft;
			y=document.all.item(capa).offsetTop;
			break;
	}
	mouse_arrastre = new Array( 1, capa, x_mouse-x, y_mouse-y );
}

//Finaliza el arrastre de la capa  (onMouseUp)
function sueltaCapa(capa){
	mouse_arrastre[0] = 0;
}

//Desplaza una capa de forma vertical/horizontal hasta alcanzar la y_ref/h_ref
function animaCapaWHMotor( capa, w_ref, h_ref, velocidad ){
	switch( document.all.item(capa).style.position ){
		case "absolute":
			w=parseInt(document.all.item(capa).style.width);
			h=parseInt(document.all.item(capa).style.height);
			break;
		case "relative":
			w=document.all.item(capa).offsetWidth;
			h=document.all.item(capa).offsetHeight;
			break;
	}

	//Desplazamos el eje y
	if( h != h_ref )
	 if( h > (h_ref + velocidad)) redimensionaCapa( capa, 0, -velocidad );
	 else if( h < (h_ref - velocidad) ) redimensionaCapa( capa, 0, velocidad );
	 else dimensionaCapa( capa, w, h_ref );
	switch( document.all.item(capa).style.position ){
		case "absolute":
			h=parseInt(document.all.item(capa).style.height);
			break;
		case "relative":
			h=document.all.item(capa).offsetHeight;
			break;
	}

	//Desplazamos el eje x
	if( w != w_ref )
	 if( w > (w_ref + velocidad)) redimensionaCapa( capa, -velocidad, 0 );
	 else if( w < (w_ref - velocidad) ) redimensionaCapa( capa, velocidad, 0 );
	 else dimensionaCapa( capa, w_ref, h );
	switch( document.all.item(capa).style.position ){
		case "absolute":
			w=parseInt(document.all.item(capa).style.width);
			break;
		case "relative":
			w=document.all.item(capa).offsetWidth;
			break;
	}

	if( (w != w_ref) || (h != h_ref) ){
		var param = new Array( "'"+capa+"'", w_ref, h_ref, velocidad );
		setTimeout( "animaCapaWHMotor("+param.join(",")+")", 20 );
	}else{
		//La capa ha finalizado su dimensionado
		var i = 0;
		//Buscamos la capa
		while( ccapa[i] != capa ) i++;
		destado[i] = 0; //La capa ahora est? en reposo
	}
}

//Desplaza una capa en los dos ejes, a una velocidad y un desplazamiento indicados
// capa = capa a animar
// tempo = temporizador a utilizar
// dx = desplazamiento en el eje x
// dy = desplazamiento en el eje y
// velocidad = diferencial de salto
function animaCapaWH( capa, dw, dh, velocidad ){
	var i = 0;
	//Buscamos un hueco libre para la capa
	while( (i < ccapa.length) && (ccapa[i] != capa) && (ccapa[i] != null) ) i++;
	if(( i < ccapa.length )	&& (destado[i] == 0)){ //Si la capa est? ya en dimensionado, no dejamos el mismo
		ccapa[i] = capa; //Asignamos la capa
		destado[i] = 1; //La capa est? en dimensionado
		switch( document.all.item(capa).style.position ){
			case "absolute":
				w=parseInt(document.all.item(capa).style.width);
				h=parseInt(document.all.item(capa).style.height);
				break;
			case "relative":
				w=document.all.item(capa).offsetWidth;
				h=document.all.item(capa).offsetHeight;
				break;
		}
		var param = new Array( "'"+capa+"'", w+dw, h+dh, velocidad );
		setTimeout( "animaCapaWHMotor("+param.join(",")+")", 20 );
	}else{
		return -1; //Avisamos del suceso
	}
}

//Esta funci?n ejecuta una aplicaci?n tanto en NS como en IE
//OJO Para que funcione hay que activar en el explorador del cliente:
//Opciones de Internet -> Seguridad -> Iniciar secuencia de comandos ActiveX marcados como no seguros
//exec('word.exe myfile.doc')
function exec(what) {
	if (document.all) {
		var wsh=new ActiveXObject("WScript.Shell");
		if (wsh) wsh.Run(what);
	}else if ((navigator.appName.indexOf("Netscape")!=-1) && navigator.javaEnabled()) {
		netscape.security.PrivilegeManager.enablePrivilege('UniversalExecAccess');
		java.lang.Runtime.getRuntime().exec(what);
	}
} 

//Abrir un doc desde Javascript (Introducir la URL donde se encuentra el documento)
function abreDoc(doc) { 
  window.open(doc,"wndWord", "toolbar=yes,location=no,menubar=yes,resizable=yes");
} 



///////////////////////////////////////////////////
//Funciones validadas hasta aqu?
///////////////////////////////////////////////////

//Comprueba si una capa no est? siendo desplazada/dimensionada/clipeada
//para impedir las operaciones en caso necesario
function capaLista( capa ){
	var i = 0;
	//Buscamos la capa para comprobar su estado
	while( (i < ccapa.length) && (ccapa[i] != capa) ) i++;
	if( i < ccapa.length ){
		if(( mestado[i] == 0 ) && ( cestado[i] == 0 ) && ( destado[i] == 0 )){
			return true; //La capa est? lista
		}else{
			return false; //La capa no est? lista
		}
	}else{
		return true; //La capa est? lista (no tiene acci?n asociada)
	}
}

