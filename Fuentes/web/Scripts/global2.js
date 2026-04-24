// -----------------------------------------------------------------------------
// Proyecto           : SUMAP
// Nombre             : global.js
// Propósito          : Aisla en un solo fichero las funciones relacionadas con la 
//						inicialización de las páginas y el mostrar el encabezado.
// Referencias        : NA
// Fecha de creación  : Raquel Trillo, 2003-11-13
// Notas              : Se trata de documentar el código de la maqueta
//                      general de aplicaciones web. 
// Historial          : NA
// ..............................................................................


//------------------------------------------------------------------------------
// Nombre      : initialize
// Propósito   : Mostrar el título de la opción elegida en el encabezado de la página.
// Retorno     : NA
// Argumentos  :
//		Modo	Nombre	  Tipo de datos	Breve explicación
//		IN      str	  	  String        Indica el título de la opción elegida.
// Referencias : NA
// FCreación   : 2003-11-13
// Notas       : NA
// Historial   : NA
// .............................................................................

function initialize(str)
{
  try
  {    
  	top.frames["topFrame"].document.getElementById("navegacion").innerHTML=str;
    //top.frames["topFrame"].navegacion.innerHTML=str;  
  
  }
  catch(err)
  {
    document.title = str;
  }
  return true;
}

//------------------------------------------------------------------------------
// Nombre      : msg
// Propósito   : Mostrar el estado elegido en la barra de estado de la ventana.
// Retorno     : NA
// Argumentos  :
//		Modo	Nombre	  Tipo de datos	Breve explicación
//		IN      str	  	  String        Indica el estado seleccionado.
// Referencias : NA
// FCreación   : 2003-11-13
// Notas       : NA
// Historial   : NA
// .............................................................................

function msg(str)
{
  window.status = str;
  return true;
}

//------------------------------------------------------------------------------
// Nombre      : getPath
// Propósito   : Obtener el string que representa la ruta actual.
// Retorno     : El string que representa la ruta actual de la página
// Argumentos  :
//		Modo	Nombre	  Tipo de datos	Breve explicación
// Referencias : NA
// FCreación   : 2003-11-13
// Notas       : NA
// Historial   : NA
// .............................................................................
function getPath()
{
  if(location.protocol == "http:")
    return location.protocol + "//" + location.hostname + location.pathname.substring(1,location.pathname.lastIndexOf("/"));
  else
    return location.pathname.substring(1,location.pathname.lastIndexOf("\\")) + "\\";
}

//------------------------------------------------------------------------------
// Nombre      : getRootPath
// Propósito   : Obtener el string que representa la ruta actual del topFrame.
// Retorno     : El string que representa la ruta actual de la página
// Argumentos  :
//		Modo	Nombre	  Tipo de datos	Breve explicación
// Referencias : NA
// FCreación   : 2003-11-13
// Notas       : NA
// Historial   : NA
// .............................................................................
function getRootPath()
{
  return top.frames["topFrame"].frmEntorno.rootPath.value;
}

//------------------------------------------------------------------------------
// Nombre      : Calendar
// Propósito   : Proporcionar calendarios para los campos fechas.
// Retorno     : Devolver unha ventana modal con el calendario cargado.
// Argumentos  :
//		Modo	Nombre	  Tipo de datos	Breve explicación
// Referencias : NA
// FCreación   : 2003-11-13
// Notas       : NA
// Historial   : NA
// .............................................................................

function Calendar()
{
 return window.showModalDialog(getRootPath() + 'scripts/calendar.htm','', 'dialogHeight:150px;dialogWidth:160px;center:yes;status:no;scroll:no');
}


	

//------------------------------------------------------------------------------
// Nombre      : ocultaCabecera
// Propósito   : Ocultar/mostrar frame izquierdo (Menu navegación)
// Retorno     : NA
// Argumentos  : NA
// Referencias : NA
// FCreación   : 2005-09-30
// Notas       : NA
// Historial   : NA
// .............................................................................

  var cabeceraOculta = 0;  
 function ocultaCabecera(){
   
   if(!cabeceraOculta){
   
	top.document.getElementsByTagName("frameset")[0].getElementsByTagName("frameset")[0].cols ="0,*";    
	cabeceraOculta = 1;
	document.btnCerrar.style.display = 'none';
    document.btnAbrir.style.display = '';  
    	
    

   }
   else{
	top.document.getElementsByTagName("frameset")[0].getElementsByTagName("frameset")[0].cols ="170,*";
    cabeceraOculta = 0;   
    document.btnAbrir.style.display = 'none';
    document.btnCerrar.style.display = '';
    

   }
   
   return;
  } 
  
  function ObtenerElmentodeColeccion(colecccion, idelemento,attributoBusqueda)
{
	var check;
	var i=0;

	for(i=0; i<colecccion.length;i++)
	{
			if (colecccion[i].getAttribute(attributoBusqueda)==idelemento)
			{			
				check = colecccion[i];
				break;
			}
	}
	
	
	return check;
}