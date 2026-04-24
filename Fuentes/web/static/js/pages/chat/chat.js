// Calcula tamaños y setea variables
document.addEventListener("DOMContentLoaded", function(event) {
  const Acciones = document.querySelector('#acciones');
	var altoAcciones = Acciones.scrollHeight;
	document.body.style.setProperty('--acciones-height', altoAcciones);
});
/******************************************************/

const inputs = [];
var flag = 0;
const userInput = document.querySelector('#userInput');
const container = document.querySelector('#contenedor');

function sendMessage() {
    //pensandoRespuesta();
    addElement();


    //alert(userInput.value);
    //console.log(window.chaturl);
    console.log("sendMessage");

    var userInputValue = "userInput="+userInput.value;

    console.log(userInput);

    $.ajax({
      url: window.chaturl,
      async: false,
      //data: userInput.value,
      //data: "userInput="+userInput.value,
      //data: 'cartValues=valorEnviado',
      data: userInputValue,
      headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      type: 'POST',
    })
    .done(function( data ) {
      if ( console && console.log ) {
        //console.log( "Sample of data:", data.slice( 0, 100 ) );
        console.log( "Sample of data:", data.responseMessage );
      }


      const pausa = getRandomIntInclusive(2, 6);

      function getRandomIntInclusive(min, max) {
        min = Math.ceil(min);
        max = Math.floor(max);
        return Math.floor(Math.random() * (max - min + 1) + min) * 1000;
      }
      
      setTimeout(function() {
        addResp(data.responseMessage);
      }, pausa);

      //addResp(data.responseMessage);
    });


    // Borra el contenido del input userInput.
    userInput.value = '';
};

// Dispara el addElement al pulsar intro
userInput.addEventListener('keypress', event => {
  if (enterPressed(event)) {
      //addElement();
      
      sendMessage();

      /*
      // Al "publicar" vuelvo a deshabilita el input y le quita el foco
      userInput.classList.add('disabled');
      userInput.blur();
      // Borra el contenido del input userInput.
      userInput.value = '';
      */
  }
});
const enterPressed = event => event.keyCode === 13 || event.which === 13;

// Función que añade el mensaje del usuario
const addElement = () => {
    const text = userInput.value;
    const element = createElement(text);

    // Por si se quierene almacenar los inputs
    inputs.push(text);
}

// Función que crea los divs del mensaje del usuario
const createElement = text => {
  /*creo el div mensaje*/
  const mensaje = document.createElement('div');
  mensaje.classList.add('mensaje');
  mensaje.classList.add('usuario');
  container.appendChild(mensaje);

  /*creo el div avatar */    
  const avatar = document.createElement('div');
  const imgAvatar = document.createElement('img');
  avatar.classList.add('avatar');
  imgAvatar.src = 'static/images/chat/logo_usuario.png';
  mensaje.appendChild(avatar);
  avatar.appendChild(imgAvatar);

  /*Creo el div Cuerpo*/
  const cuerpo = document.createElement('div');
  cuerpo.classList.add('cuerpo');
  mensaje.appendChild(cuerpo);

  /*Creo el div texto, le añado el texto del input y lo meto dentro de cuerpo */
  const el = document.createElement('div');
  el.innerHTML = text;
  el.classList.add('texto');
  cuerpo.appendChild(el);

  /*Creo el div de la fecha, le añado la fecha de ahora y lo meto dentro de cuerpo */
  const fecha = document.createElement('div');
  let meses = new Array ("Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic");
  var ahora = new Date();
  const hora = ahora.getHours();
  var AMPM = 'AM'

  if (hora > 12) {
      horaAMPM = (hora - 12);
      AMPM = 'PM'
  }
  else {
    horaAMPM = hora;
  }

  fecha.innerHTML = ahora.getDate() + " " + meses[(ahora.getMonth() +1)] + " " + ahora.getFullYear() + ", " + horaAMPM + ":" + ahora.getMinutes() + " " + AMPM;
  fecha.classList.add('fecha');
  cuerpo.appendChild(fecha);
  
  var flag = 0;

// Al "publicar" el mensaje del usuario, hago scroll hasta abajo
  container.scrollTo(0, container.scrollHeight);

// lanza la autorespuesta*/
  if (flag = 1) {
    autoResp();
  }
}

/* Autorespuesta */
function autoResp() {
    pensandoRespuesta(); 
};

// Crea el div Pensando con el loader */
function pensandoRespuesta() {
  const pensando = document.createElement('div');
  pensando.classList.add('pensando');
  container.appendChild(pensando);

  /*const pausa = getRandomIntInclusive(2, 6);

  function getRandomIntInclusive(min, max) {
    min = Math.ceil(min);
    max = Math.floor(max);
    return Math.floor(Math.random() * (max - min + 1) + min) * 1000;
  }
  setTimeout(addResp, pausa);
  */
  //container.scrollTo(0, container.scrollHeight);
};

/*********************/

// Función que añade el mensaje de soporte
const addResp = (text) => {

  const pensando = document.querySelector('.pensando');
  pensando.style.display = 'none';
  pensando.classList.remove('pensando');
  //const text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque mollis id urna id accumsan. Etiam efficitur ac ex pretium pulvinar. Vivamus porta ante pretium faucibus aliquet.";
  const response = createResponse(text);
}

// Función que crea los divs del mensaje de soporte
const createResponse = text => {
    
  /*creo el div mensaje*/
  const mensaje = document.createElement('div');
  mensaje.classList.add('mensaje');
  mensaje.classList.add('soporte');
  container.appendChild(mensaje);

  /*creo el div avatar */    
  const avatar = document.createElement('div');
  const imgAvatar = document.createElement('img');
  avatar.classList.add('avatar');
  imgAvatar.src = 'static/images/chat/bot.jpg';
  mensaje.appendChild(avatar);
  avatar.appendChild(imgAvatar);

  /*Creo el div Cuerpo*/
  const cuerpo = document.createElement('div');
  cuerpo.classList.add('cuerpo');
  mensaje.appendChild(cuerpo);

  /*Creo el div texto, le añado el texto del input y lo meto dentro de cuerpo */
  const el = document.createElement('div');
  el.innerHTML = text;
  el.classList.add('texto');
  cuerpo.appendChild(el);

  /*Creo el div de la fecha, le añado la fecha de ahora y lo meto dentro de cuerpo */
  const fecha = document.createElement('div');
  let meses = new Array ("Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic");
  var ahora = new Date();
  const hora = ahora.getHours();
  var AMPM = 'AM'

  if (hora > 12) {
      horaAMPM = (hora - 12);
      AMPM = 'PM'
  }
  else {
    horaAMPM = hora;
  }

  fecha.innerHTML = ahora.getDate() + " " + meses[(ahora.getMonth() +1)] + " " + ahora.getFullYear() + ", " + horaAMPM + ":" + ahora.getMinutes() + " " + AMPM;
  fecha.classList.add('fecha');
  cuerpo.appendChild(fecha);

  var flag = 0;

  // Al "publicar" la respuesta, hago scroll hasta abajo
  container.scrollTo(0, container.scrollHeight);
  // Al "publicar" vuelvo a habilitar el input y le pongo el foco
  userInput.classList.remove('disabled');
  userInput.focus();
}


/** Botón Scroll to bottom */
let ScrollB = document.getElementById("scrollBottom");


var scrollPos = 0;
window.addEventListener('scroll', function(){
  if ((container.getBoundingClientRect()).top > scrollPos)
		// ScrollB.style.display = "block";
    console.log('Vas hacia arriba');
	else
		document.getElementById('box').innerText = '↓';
    // ScrollB.style.display = "none";
    console.log('Vas hacia abajo');
});


// Cuando se pulsa el scroll_bottom, hace scroll hasta el último mensaje
function bottomFunction() {
  container.scrollTo({top: container.scrollHeight, behavior: 'smooth'});
}


// Comprueba si se ve el primer mensaje
var isInViewport = function(elem) {
  var distance = elem.getBoundingClientRect();
  return (
    distance.top >= 0 &&
    distance.left >= 0 &&
    distance.bottom <= (window.innerHeight || document.documentElement.clientHeight) &&
    distance.right <= (window.innerWidth || document.documentElement.clientWidth)
  );
};

/* Muestra u oculta el botón de scrollBottom */
container.addEventListener('scroll', function () {
  var reciente = document.querySelectorAll('#reciente-mensaje');

  // Detecto cuando entra el primer-mensaje en el vieport
  reciente.forEach(element => {
    //for each .compra
    if (isInViewport(element)) {
      ScrollB.style.display = "none";
    }
    else {
      ScrollB.style.display = "block";
    }
  });
});