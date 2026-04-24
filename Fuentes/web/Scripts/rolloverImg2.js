
// -----------------------------------------------------------------------------
// Proyecto           : SUMAP
// Nombre             : folloverImg.js
// Propósito          : Aisla en un solo fichero las funciones generadas por el 
//                      Dreamweaver para los efectos de rollover sobre imágenes.
// Referencias        : NA
// Fecha de creación  : Raquel Trillo, 2003-11-13
// Notas              : Se trata de documentar el código del catálogo 
//                      general de productos del Sergas hecho por Marcos. 
// Historial          : NA
// ..............................................................................


//------------------------------------------------------------------------------
// Nombre      : MM_preloadImages
// Propósito   : Cargar las imágenes que se van a utilizar cuando se llama a esta
//               función.
// Retorno     : NA
// Argumentos  :
//		Modo	Nombre	  Tipo de datos	Breve explicación
//		IN      ninguno	  Array         Nombre de las imagenes que se 
//                                              desean cargar en el documento.
// Referencias : NA
// FCreación   : Marcos, ¿1999-06-15?
// Notas       : NA
// Historial   : NA
// .............................................................................

function MM_preloadImages() 
{   //v3.0
    var d=document; 
    if(d.images)
    { 
        if(!d.MM_p) d.MM_p=new Array();
        var i,j=d.MM_p.length,a=MM_preloadImages.arguments;
        for(i=0; i<a.length; i++)
          if (a[i].indexOf("#")!=0)
          {
              d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];
          }
    }
}

//------------------------------------------------------------------------------
// Nombre      : MM_reloadPage
// Propósito   : Recargar la página actual.
// Retorno     : NA
// Argumentos  :
//		Modo	Nombre	  Tipo de datos	Breve explicación
//		IN      init	  Boolean       Si se está iniciando o no, es decir
//                                              si es la primera vez o no.
// Referencias : NA
// FCreación   : Marcos, ¿1999-06-15?
// Notas       : NA
// Historial   : NA
// .............................................................................
function MM_reloadPage(init) 
{  //reloads the window if Nav4 resized
    if (init==true) with (navigator) 
    {
        if ((appName=="Netscape")&&(parseInt(appVersion)==4)) 
        {
            document.MM_pgW=innerWidth; 
            document.MM_pgH=innerHeight; 
            onresize=MM_reloadPage; 
        }
    }
    else 
        if (innerWidth!=document.MM_pgW || innerHeight!=document.MM_pgH) 
            location.reload();
}
MM_reloadPage(true);

//------------------------------------------------------------------------------
// Nombre      : MM_swapImgRestore
// Propósito   : 
// Retorno     : NA
// Argumentos  :
//		Modo	Nombre	  Tipo de datos	Breve explicación
//		IN      ninguno	  Image         Objeto imagen del que hay que cambiar
//                                              su fuente por la que estaba originalmente.
// Referencias : NA
// FCreación   : Marcos, ¿1999-06-15?
// Notas       : NA
// Historial   : NA
// .............................................................................
function MM_swapImgRestore() 
{ //v3.0
    var i,x,a=document.MM_sr; 
    for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) 
        x.src=x.oSrc;
}
//------------------------------------------------------------------------------
// Nombre      : MM_findObj
// Propósito   : Encontrar el objeto especificado.
// Retorno     : El objeto que se buscaba.
// Argumentos  :
//		Modo	Nombre	  Tipo de datos	Breve explicación
//		IN      n	  String        Referencia al objeto que se busca.
//		IN      d	  document	Documento con el que se trabaja
// Referencias : NA
// FCreación   : Marcos, ¿1999-06-15?
// Notas       : NA
// Historial   : NA
// .............................................................................
function MM_findObj(n, d) 
{ //v4.01
    var p,i,x;  
    if(!d) d=document; 
    if((p=n.indexOf("?"))>0&&parent.frames.length) 
    {
        d=parent.frames[n.substring(p+1)].document; 
        n=n.substring(0,p);
    }
    if(!(x=d[n])&&d.all) x=d.all[n]; 
    for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
    for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
    if(!x && d.getElementById) x=d.getElementById(n); 
    return x;
}

//------------------------------------------------------------------------------
// Nombre      : MM_swapImage
// Propósito   : 
// Retorno     : NA
// Argumentos  :
//		Modo	Nombre	  Tipo de datos	Breve explicación
//		IN      ninguno	  String(Varios)Nombres de los objetos imagenes 
//                                              de los que hay que cambiar su fuente.
//              IN      ninguno   Int           Número de objetos imagen de los 
//                                              que hay que cambiar su fuente.
//              IN      ninguno   String(Varios)Path de las imágenes que se desean
//                                              poner.
// Referencias : NA
// FCreación   : Marcos, ¿1999-06-15?
// Notas       : NA
// Historial   : NA
// .............................................................................
function MM_swapImage()
{ //v3.0
    var i,j=0,x,a=MM_swapImage.arguments; 
    document.MM_sr=new Array; 
    for(i=0;i<(a.length-2);i+=3)
       if ((x=MM_findObj(a[i]))!=null)
       {
            document.MM_sr[j++]=x; 
            if(!x.oSrc) x.oSrc=x.src; 
            x.src=a[i+2];
       }
  //  alert('Estou en MM_swapImage vlae?')
   
}

//------------------------------------------------------------------------------
// Nombre      : MM_showHideLayers
// Propósito   : 
// Retorno     : NA
// Argumentos  :
//		Modo	Nombre	  Tipo de datos	Breve explicación
//		IN      ninguno	  String(Varios)Nombres de los objetos imagenes 
//                                              de los que hay que cambiar su fuente.
//              IN      ninguno   Int           Número de objetos imagen de los 
//                                              que hay que cambiar su fuente.
// Referencias : NA
// FCreación   : Marcos, ¿1999-06-15?
// Notas       : NA
// Historial   : NA
// .............................................................................
function MM_showHideLayers() 
{ //v6.0
    var i,j,p,v,obj,args=MM_showHideLayers.arguments;
    for (i=0; i<(args.length-2); i+=3) 
        if ((obj=MM_findObj(args[i]))!=null) 
  	{
  	    v=args[i+2];
    	    if (obj.style) 
    	    {
    	        if (args[3]!=null) //Marcos: Ponemos el texto que pasamos como argumento
    		    if (args[3].length > 0) 
    		    {
    		        obj.value = args[3];
    			for (j=args[3].length; j<200; j++) 
    			// Marcos: Rellenamos de espacios por la derecha, para que no aparezcan lineas cortadas
    			    obj.value = obj.value + " ";
    		    }
    		obj=obj.style; v=(v=='show')?'visible':(v=='hide')?'hidden':v; 
    	    }
    	    obj.visibility=v; 
        }
}