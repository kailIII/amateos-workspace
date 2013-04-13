// ==UserScript==
// @name                Borra-P
// @namespace           AlbertoMateos.org/GreaseMonkey
// @description         Borra <p>
// @include             http://geneura.ugr.es/~jmerelo/asignaturas/AAP/*
// ==/UserScript==

var p = document.getElementsByTagName('p');

for ( var i = 0; i < p.length; i ++ ) {
	p[i].innerHTML="";
}
