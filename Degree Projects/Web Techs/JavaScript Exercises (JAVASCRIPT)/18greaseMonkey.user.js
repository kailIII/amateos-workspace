// ==UserScript==
// @name                AAP-Nav-Ejercicios
// @namespace           http://geneura.org/projects/greasemonkey
// @description         NavegaciÛn por los ejercicios de AAP
// @include             http://geneura.ugr.es/~jmerelo/asignaturas/*
// ==/UserScript==

var h3 = document.getElementsByTagName('h3');

for ( var ejer = 0; ejer < h3.length; ejer ++ ) {
	var span = document.createElement('span');
	span.setAttribute('style','background:lightblue');
	if ( ejer > 0 ) {
		var ahref = document.createElement('a');
		ahref.setAttribute('href','#ej.T1.'+ejer);
		var txt=document.createTextNode('^');
		ahref.appendChild(txt);
		span.appendChild(ahref);
	}
	if ( ejer < h3.length -1  ) {
		span.appendChild(document.createTextNode(' | '));
		var ahref = document.createElement('a');
		ahref.setAttribute('href','#ej.T1.'+(ejer+2));
		var txt=document.createTextNode('v');
		ahref.appendChild(txt);
		span.appendChild(ahref);
	}
	h3[ejer].parentNode.insertBefore(span,h3[ejer]);
}
