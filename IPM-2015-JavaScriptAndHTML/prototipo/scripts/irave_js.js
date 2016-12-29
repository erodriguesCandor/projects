/*** RELOGIO ***/
var palcos = ["Palco Principal", "Palco Pimba", "Palco Rap"];
var avanco = 0;

function start(){
	var d = new Date();
	window.h = d.getHours();
	window.m = d.getMinutes();
	startTime();
	fillItems();
	overflow();
}

function fillItems(){
	var x = document.querySelectorAll(".lista_precos li");
	for(i = (x.length-1); i >= 0; i--){
		x[i].innerHTML = x[i].getAttribute("name") + " <span style=\"float:right\">" + x[i].getAttribute("price") + "€</span>"; 
	}
	x =	document.querySelectorAll(".musica li");
	for(i = (x.length-1); i >= 0; i--){
		x[i].innerHTML = x[i].getAttribute("name"); 
	}
}

function overflow(){
	var loc = document.querySelectorAll('.localizacao');
	for(i = (loc.length-1); i >= 0; i--){
		if(loc[i].clientWidth < loc[i].scrollWidth || loc[i].clientHeight < loc[i].scrollHeight)
			loc[i].innerHTML = '<marquee behavior="scroll" direction="left" scrollamount="2">' + loc[i].innerHTML + '</marquee>';
	}
}

function toque(){			
	var n = document.getElementById("notificacoes").getAttribute("n").valueOf();
	if(n == 0) document.querySelector("#notificacoes ul").innerHTML = "";
	document.getElementById("notificacoes").setAttribute("n", ++n);
	document.querySelector("#notificacoes ul").innerHTML += "<li>Ana Malhoa mandou-lhe um toque</li>";
	document.getElementById("top2").style.visibility = "visible";
	document.getElementsByClassName("nalertas")[0].style.visibility = "visible";
	if(n < 10) document.getElementsByClassName("nalertas")[0].innerHTML = n;
	else document.getElementsByClassName("nalertas")[0].innerHTML = "9+";
}

function startTime() {
	var today=new Date();
	if(today.getSeconds() < 3) window.m++;
	today.setHours(window.h);
	today.setMinutes(window.m);
	window.h = today.getHours();
	window.m = today.getMinutes();
	var h = window.h;
	var m = checkTime(window.m);
	var x = document.getElementsByClassName("hora");
	for(i = (x.length)-1; i >= 0; i--){ x[i].innerHTML = h+":"+m; }
	x = document.getElementsByClassName("espera");
	for(i = (x.length)-1; i >= 0; i--){
		if(x[i].getAttribute("tempo")/100 < h || x[i].getAttribute("tempo")%100 < m){
			var txt = x[i].parentNode.parentNode.innerHTML;
			
			var n = document.getElementById("notificacoes").getAttribute("n").valueOf();
			if(n == 0) document.querySelector("#notificacoes ul").innerHTML = "";
			document.getElementById("notificacoes").setAttribute("n", ++n);
			document.getElementsByClassName("nalertas")[0].style.visibility = "visible";
			if(n < 10) document.getElementsByClassName("nalertas")[0].innerHTML = n;
			else document.getElementsByClassName("nalertas")[0].innerHTML = "9+";
			
			n = x[i].parentNode.parentNode.parentNode.parentNode.getAttribute("n").valueOf();
			x[i].parentNode.parentNode.parentNode.parentNode.setAttribute("n", --n);
			
			if(x[i].getAttribute("origin") == "diversoes")
				document.querySelector("#notificacoes ul").innerHTML += "<li>É a sua vez no(a) <i>"+x[i].getAttribute("info")+"</i>.</li>";
			else if(x[i].getAttribute("origin") == "comida")
				document.querySelector("#notificacoes ul").innerHTML += "<li>O seu pedido da loja <i>"+x[i].getAttribute("info")+"</i> está pronto</li>";
			
			document.getElementById("top2").style.visibility = "visible";
			txt = txt.replace("<li>" + x[i].parentNode.innerHTML + "</li>", '');
			if(n == 0) x[i].parentNode.parentNode.innerHTML = "<li>Sem pedidos</li>";
			else x[i].parentNode.parentNode.innerHTML = txt;
		}
		else{
			var t = x[i].getAttribute("tempo")%100 - m;
			if(t < 0) t += 60;
			x[i].innerHTML = t + " min";
		}
	}
	for( i = 0; i <= 2; i++ ){
		x = document.getElementById("concertos_tocar_palco" + i).getElementsByClassName("txt")[0];
		if( h >= 19 && h < 22){
			var loc = document.getElementById(("concertos_palco" + i) + (h-19) + "_musica").getElementsByTagName("li")[Math.floor(m/20)];
			x.innerHTML = "<u>"+palcos[i] + "</u><br/>" + loc.getAttribute("band") + "<br/>" +  loc.getAttribute("name");
		}
		else{
			x.innerHTML = "<u>"+palcos[i] + "</u><br/><br/>Não há bandas a tocar no palco";
		}
	}
	setTimeout(function(){startTime()},3000);
}

function checkTime(i) {
	if (i<10) {i = "0" + i};
	return i;
}

function finish(val){
	var e = document.getElementById("conclusao").getElementsByClassName("txt")[0];
	setTimeout(function(){ e.innerHTML = "Operação concluída com sucesso"; }, 1000);
	setTimeout(function(){ e.innerHTML = "Senha nº " + val + "<br/>Tempo de espera: 5 min"; document.getElementById("conclusao").getElementsByClassName("footer")[0].style.visibility = "visible";}, 3000);
	return val;
}

function finishTxt(txt){
	var e = document.getElementById("conclusao").getElementsByClassName("txt")[0];
	setTimeout(function(){ e.innerHTML = txt; document.getElementById("conclusao").getElementsByClassName("footer")[0].style.visibility = "visible";}, 1000);
	return val;
}

function goToFuture(time){
	window.m += time;
}

function now(){ avanco = 0; startTime(); }
