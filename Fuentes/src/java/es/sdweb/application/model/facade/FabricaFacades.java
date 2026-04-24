package es.sdweb.application.model.facade;

import es.sdweb.application.componentes.util.ObjectUtil;
import es.sdweb.application.model.exceptions.ExceptionErrorInterno;
import es.sdweb.application.util.ExceptionFaltaParametro;
import es.sdweb.application.util.GestorParametrosConfiguracion;
import java.util.logging.Level;
import java.util.logging.Logger;


public abstract class FabricaFacades {

  private final static String PREFIJO_PARAMETRO_CONFIGURACION_FACADE = FabricaFacades.class.getPackage().getName() + ".";
  private final static String SUBFIJO_PARAMETRO_CONFIGURACION_FACADE = ".className";
  
  private final static String GESTIONUSUARIO_FACADE =  "GestionUsuarioFacade";
  private final static String MANTENIMIENTO_FACADE =  "MantenimientoFacade";
  private final static String TAQUILLA_CONTROL_FACADE =  "TaquillaControlFacade";
  private final static String EXPLOTACION_DATOS_FACADE =  "ExplotacionDatosFacade";
  private final static String PORTAL_FACADE =  "PortalFacade";

  private static Object crearFacade(String nombreFacade) throws ExceptionErrorInterno{
      Object result=null;
      String nomParametro=PREFIJO_PARAMETRO_CONFIGURACION_FACADE+nombreFacade+
      SUBFIJO_PARAMETRO_CONFIGURACION_FACADE;

      String daoClassName=null;
        try {
            daoClassName = GestorParametrosConfiguracion.getParametro(nomParametro,true);
        } catch (ExceptionFaltaParametro ex) {
            Logger.getLogger(FabricaFacades.class.getName()).log(Level.SEVERE, null, ex);
        }

      result=ObjectUtil.createInstance(daoClassName);

      return result;
  }

  public static IGestionUsuarioFacade createGestionUsuarioFacade() throws ExceptionErrorInterno{

          return (IGestionUsuarioFacade)crearFacade(GESTIONUSUARIO_FACADE);
  }

  public static IMantenimientoFacade createMantenimientoFacade() throws ExceptionErrorInterno{

          return (IMantenimientoFacade)crearFacade(MANTENIMIENTO_FACADE);
  }
  
  public static IExplotacionDatosFacade createExplotacionDatosFacade() throws ExceptionErrorInterno{

          return (IExplotacionDatosFacade)crearFacade(EXPLOTACION_DATOS_FACADE);
  }

}//class



