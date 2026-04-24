/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sdweb.application.model.facade;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import es.sdweb.application.model.dto.ConstanteDTO;
import es.sdweb.application.model.dto.UsrDTO;
import es.sdweb.application.model.exceptions.ExceptionErrorInterno;
import es.sdweb.application.util.GestorParametrosConfiguracion;

/**
 *
 * @author Antonio Carro Mariño
 */
public abstract class UtilFacade {

    public static final String PARAMETRO_APPLICATION_NAME="application.name";

    public static final String CONSTANTE_LOCALE_DEFECTO="LOCALE.DEFECTO"; //Clave para obtener el Locale por defecto para fechas y cifras economicas.
    public static final String CONSTANTE_LOCALIDAD_COLOR_RESERVADA="COLOR.BUTACA.RESERVADA"; //COLOR DE FONDO DE LAS BUTACAS RESERVADAS PARA UNA FUNCION.
    public static final String CONSTANTE_NUMERO_HORAS_RESERVA="NUMERO.HORAS.RESERVA"; //Numero maximo de horas que se sostendra una reserva hasta que se desbloquee de forma automatica.
    public static final String CONSTANTE_TIPO_OPERACION_VENTA="IDENTIFICADOR.TIPO.OPERACION.VENTA"; //IDENTIFICADOR DEL TIPO DE OPERACION VENTA.
    public static final String CONSTANTE_TIPO_OPERACION_CANCELACION_VENTA="IDENTIFICADOR.TIPO.OPERACION.VENTA.CANCELACION"; //IDENTIFICADOR DEL TIPO DE OPERACION CANCELACION DE VENTA.
    public static final String CONSTANTE_TIPO_OPERACION_RECARGA_SALDO="IDENTIFICADOR.TIPO.OPERACION.RECARGA.SALDO"; //IDENTIFICADOR DEL TIPO DE OPERACION DE RECARGA DE SALDO.
    public static final String CONSTANTE_TIPO_OPERACION_COPIA_TARJETA="IDENTIFICADOR.TIPO.OPERACION.COPIA.TARJETA"; //IDENTIFICADOR DEL TIPO DE OPERACION CAJA COPIA DE TARJETA.
    public static final String CONSTANTE_TIPO_OPERACION_EMISION_DEVOLUCION_TARJETA="IDENTIFICADOR.TIPO.OPERACION.EMISION.DEVOLUCION.TARJETA"; //IDENTIFICADOR DEL TIPO DE OPERACION CAJA EMISION/DEVOLUCION DE TARJETA.
    public static final String CONSTANTE_FORMA_PAGO_TARJETA_ABONADO="IDENTIFICADOR.FORMA.PAGO.TARJETA.ABONADO"; //Para comprobar que solo los abonados pagan con esta modalidad
    public static final String CONSTANTE_FORMA_PAGO_ABONO = "IDENTIFICADOR.FORMA.PAGO.ABONO"; //Para comprobar que se paga con un abono.
    public static final String CONSTANTE_IMPORTE_EMISION_DEVOLUCION_TARJETA_ABONADO="IMPORTE.EMISION.DEVOLUCION.TARJETA.ABONADO"; //IMPORTE DE EMITIR/DEVOLVER LA TARJETA DE ABONADO EN EUROS.
    public static final String CONSTANTE_IMPORTE_COPIA_TARJETA_ABONADO="IMPORTE.COPIA.TARJETA.ABONADO"; //IMPORTE DE EMITIR UNA COPIA DE LA TARJETA DE ABONADO EN EUROS.
    public static final String CONSTANTE_TIPO_OPERACION_ABONO="IDENTIFICADOR.TIPO.OPERACION.ABONO"; //IDENTIFICADOR DEL TIPO DE OPERACIÓN VENTA DE ABONO.

    public static final Locale LOCALE_DEFECTO = getDefaultLocale(); //Locale por defecto para fechas y cifras economicas. Se carga al iniciarse la Aplicacion

    /**
     * Devuelve el Locale por defecto de la aplicacion. El Locale se usa para formatear adecuadamente numero y fechas.
     * Lee el locale de la clave LOCALE.DEFECTO en la tabla de constantes. Si no esta definida esa entrada utiliza el Locale "es_ES".
     * @param usr Usuario que solicita la operacion.
     * @return Locale de la Aplicacion
     * @throws ExceptionErrorInterno En caso de producirse un error.
     */
    private static Locale getDefaultLocale(){
       Locale result=null;
       try {
            IMantenimientoFacade iMantenimientoFacade = FabricaFacades.createMantenimientoFacade();
            UsrDTO usr=new UsrDTO();
            usr.setUsr(GestorParametrosConfiguracion.getParametro(PARAMETRO_APPLICATION_NAME));
            ConstanteDTO cte=iMantenimientoFacade.getConstante(usr, CONSTANTE_LOCALE_DEFECTO);
            if (cte==null){
                result=new Locale("es_ES");
            }else{
                result=new Locale(cte.getValor());
            }
        } catch (ExceptionErrorInterno ex) {
            Logger.getLogger(UtilFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
      return result;
    }

}//class
