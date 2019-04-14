//declaring constants

//all variables are readonly UNLESS JavaScript is enabled
//create FORMUSERNAME after removing readonly attribute

const FORMUSERNAME = (function () { 
  //grab username inpute
  let el = document.querySelector('input#userName')
  el.removeAttribute('readonly');
  return el;
})();

//create FORMPASSWORD after removing readonly attribute

const FORMPASSWORD = (function () {
  //grab password input
  let el = document.querySelector('input#password');
  el.removeAttribute('readonly');
  return el;
})();

//create FORM, do not need to remove readonly
const FORM = document.querySelector('form#loginForm');


//freezing inputs to prevent ids from changing after rendering
Object.freeze( FORMUSERNAME );
Object.freeze( FORMPASSWORD );

function testFields ( event ) {

  let userName = FORMUSERNAME.value;
  let password = FORMPASSWORD.value;
  let error = document.querySelector('span.loginErrorSpan'); 
  let result;

  error.innerHTML = '';

  function checkUserName ( userName ) {
    //check if username has spaces, which aren't allowed
    if ( / /.test ( userName ) ) {
      //update error span
      error.innerHTML += "<li class='error'>Username cannot contain spaces</li>";
      //set result = true, which will prevent default and stop form submission
      result = true;
    }

    //check whether username is at least 4 characters
    if ( ! ( /.{4}/.test( userName ) ) ) {
      error.innerHTML += "<li class='error'>Username must be at least 4 characters</li>";
      result = true;
    }

    //checks whether special characters are in username, also not allowed
    if (/(\W|_)/.test ( userName ) ) {
      error.innerHTML += "<li class='error' >Username cannot have special characters</li>";
      result = true;
    }

    return result;
  }

//function: checkpassword
//Verifies that the supplied password is valid and matches parameters.
  function checkPassword ( password ) {
      //check for at least 6 characters in password field
      if ( ! ( /\w{6}/.test( password ) ) ) {
        error.innerHTML += "<li class='error' >Password less than 6 characters</li>";
        result = true;
      }
      
      //checks whether or not password has special characters, which it must contain at least 1
      if ( ! ( /\W/.test ( password ) ) ) {
        error.innerHTML += "<li class='error'>Password must contain special character</li>";
        result = true;
      }

    return result;
  }
  



//Checks the username and password of the form, if errors prevents default and displays errors
  checkUserName ( userName );
  checkPassword ( password );

//if result is false, then no errors, so form is allowed to post, result is cleared
  if ( !result ) {

    result = '';

  } else {
//by default, form will display errors and  prevent submission
    error.display = "block"
    event.preventDefault();

  }

}

//adds event listener to FORM element

FORM.addEventListener('submit', function (event) {testFields(event)}, false);





