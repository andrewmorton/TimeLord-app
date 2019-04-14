const RESETBUTTON = document.querySelector('input#reset');

function resetTextAreas () {
  let textActions = document.querySelector('textarea#textActions');
  let textNextSteps = document.querySelector('texarea#textNextSteps');

  textActions.content = "";
  textNextSteps.content = "";
}


RESETBUTTON.addEventListener('onclick', resetTextAreas(), false);