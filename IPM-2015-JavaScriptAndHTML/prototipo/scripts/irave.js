var unlock = "#slick_principal";
var time = 0;
var mostrarTodos = false;
var val = Math.floor(Math.random() * 10000 + 1);
var comida_prefixo = '';
var conclusao_prefixo = '';
var concertos_historico = '#concertos_tocar_palco0';
var preferencias = false;
var cancelar = false;
var cancelar_sim = '';
var cancelar_nao = '';
var produto = '';
var lanterna = 0;
var remover = 0;
var aprox = 0;
var ajuda = '';
var loja = '';

$.fn.limpar = function(){
	$(".lista_precos li").removeClass("selected").attr("n", 0);
	$("#conclusao .txt").html('A processar...<br/><br/><img src="images/loader.gif" width="40%" height="40%"/>');
	$("#conclusao .footer").css("visibility", "hidden");
	$(".lista_final").html('<li id="total">Total <span style="float:right">€</span><span class="tt" style="float:right">0</span></li>');
	$(".lista_precos").parent().parent().find(".sim, .sim2").each(function(){ $(this).removeClass("verde"); });
	$("#amigos_sugerir1 .selected").removeClass("selected");
	$("#amigos_sugerir2 .selected").removeClass("selected");
	$("#amigos_sugerir1 .sim2").removeClass("verde");
	$("#amigos_sugerir2 .sim2").removeClass("verde");
	cancelar = false;
}

$.fn.foodManager = function(comida_prefixo, tipo){
	var menus = ["bebidas", "bifanas", "extras", "cafes", "tostas", "batidos", "baguetes", "sopas", "aperitivos"];
	if( tipo == "menu") {
		for(var i = menus.length-1; i >= 0; i--){
			if($(this).attr("id") == comida_prefixo+"_encomendar_"+menus[i]){	$(this).changeTab("#"+comida_prefixo+"_"+menus[i]); cancelar_nao = "#"+comida_prefixo+"_"+menus[i]; cancelar_sim = "#lojas_"+comida_prefixo;}
		}
	} else if(tipo == "sim") {
		for(var i = menus.length-1; i >= 0; i--){
			if($(this).attr("id") == comida_prefixo+"_"+menus[i]){	$(this).changeTab("#verificacao"); cancelar_nao="#verificacao";}
		}
	} else if(tipo == "nao") {
		for(var i = menus.length-1; i >= 0; i--){
			if($(this).attr("id") == comida_prefixo+"_"+menus[i]){ $(this).changeTab("#"+comida_prefixo+"_encomendar"); cancelar_sim="#lojas_"+comida_prefixo; cancelar_nao = "#"+comida_prefixo+"_encomendar";}
		}
	};					
}

$.fn.changeTab = function(tab){
	
	$(tab).show().siblings().hide();
	if( $(tab).hasClass("slick") ){
		$(tab).slick("slickGoTo", 0, true);
	};
	
	if (tab != "#ajuda" && tab != "#ajuda_botoes" && tab != "#slick_ajuda_menus" && tab != "#slick_ajuda_setas" && tab != "#ajuda_leds" && tab != "#black_screen" && tab != "#slick_lockscreen"){
		if( $(tab).find(".menu").length != 0 ){
			while( $("#slick_ajuda_menus").find(".caixa").length != 1 )
				$("#slick_ajuda_menus").slick("slickRemove", 0);
			$("#slick_ajuda_menus .corpo").text("");	
			if ( $('#ajuda li').length == 3 ) $("#ajuda ul").prepend('<li class="vai_ajuda_menus">Menus</li>');
				
			$(tab).find(".menu").each(function(){
				if( $(this).attr("info") != "" ) {
					if ( $("#slick_ajuda_menus .corpo").eq(0).html() == "" )
						$("#slick_ajuda_menus .corpo").html('<div class="perfil"><div>'+ $(this).find(".legenda").text() +'</div><div><img src="'+ $(this).find("img").attr("src") +'"/></div></div><div class="txt">'+ $(this).attr("info") +'</div>');
					else
						$("#slick_ajuda_menus").slick("slickAdd", '<div class="caixa"></div><div id="ajuda_menus" class="caixa"><div class="header"><div class="hora"></div><div class="localizacao">Ajuda</div></div><div class="corpo"><div class="perfil"><div>'+ $(this).find(".legenda").text() +'</div><div><img src="'+ $(this).find("img").attr("src") +'"/></div></div><div class="txt">'+ $(this).attr("info") +'</div></div></div></div>');
				}
			});
		}
		else if ( $('#ajuda li').length == 4 ) {	
			$('#ajuda li:first-child').remove();
		};
	};
	
	$(this).newMap();
}

$.fn.addFriend = function(fName, img){
	if($("#slick_lista .caixa").hasClass("sem_amigo")) {
		$("#slick_lista .corpo").replaceWith('<div class="corpo perfil com_footer"><div>'+fName+'</div><div><img src="'+img+'"/></div></div><div class="footer"><div class="outros vermelho">Remover</div><div class="sim2 verde">Toque</div></div>');
		$("#slick_toque .corpo").replaceWith('<div class="corpo perfil com_footer"><div>'+fName+'</div><div><img src="'+img+'"/></div></div><div class="footer"><div class="sim verde">Enviar toque</div></div>');
		$("#mapa_amigos .corpo").replaceWith('<div class="corpo"><ul class="lista touchable"><li class="vai_mapa_amigos">'+fName+'</li></ul></div>');
		$("#slick_lista .caixa").removeClass("sem_amigo");
	} else {
		$("#slick_lista").slick("slickAdd", '<div class="caixa"></div><div class="caixa amigo"><div class="header"><div class="hora"></div><div class="localizacao">Lista amigos</div></div><div class="corpo perfil com_footer"><div>'+fName+'</div><div><img src="'+img+'"/></div></div><div class="footer"><div class="outros vermelho">Remover</div><div class="sim2 verde">Toque</div></div></div>');
		$("#slick_toque").slick("slickAdd", '<div class="caixa"></div><div class="caixa amigo"><div class="header"><div class="hora"></div><div class="localizacao">Toque</div></div><div class="corpo perfil com_footer"><div>'+fName+'</div><div><img src="'+img+'"/></div></div><div class="footer"><div class="sim verde">Enviar toque</div></div></div>');
		$("#mapa_amigos ul").append('<li class="vai_mapa_amigos">'+fName+'</li>');
	};
}

$.fn.rmFriend = function(){
	if($("#slick_lista .caixa").length == 1) {
		$("#slick_lista .caixa").find(".corpo").replaceWith('<div class="corpo"><div class="txt">Não tem amigos</div></div></div>');
		$("#slick_toque .caixa").find(".corpo").replaceWith('<div class="corpo"><div class="txt">Não tem amigos</div></div></div>');
		$("#mapa_amigos .corpo").replaceWith('<div class="corpo"><div class="txt">Não tem amigos</div></div></div>');
		$("#slick_lista .caixa").addClass("sem_amigo");
	} else {
		if(remover != 0){ remover--; };
		$(this).changeTab("#slick_toque");
		$("#slick_toque").slick("slickRemove", remover);
		$("#slick_toque").slick("slickRemove", remover);
		$("#slick_toque .caixa").css("width", "1.06in");
		
		setTimeout( function(){
			$(this).changeTab("#slick_lista");
			$("#slick_lista").slick("slickRemove", remover);
			if($("#slick_lista .caixa").length != 0) $("#slick_lista").slick("slickRemove", remover);
			$("#slick_lista .caixa").css("width", "1.06in");
		}, 10);

		setTimeout( function(){
			remover = Math.ceil(remover/2)+1;
			$('#mapa_amigos ul li:nth-child('+remover+')').remove();
		}, 20);
	};
	
	setTimeout(function(){remover = 0;}, 30);
}

$.fn.newMap = function(){
	$(".mapa_arrow").css("transform", "rotate("+(Math.random()*360)+"deg)");
	$(".mapa_dist").text(Math.floor(Math.random()*100)+" metros");
}

$.fn.addPhoto = function(fName, img){
	if($("#slick_galeria .caixa").hasClass("sem_fotos")) {
		$("#slick_galeria .corpo").replaceWith('<div class="corpo com_footer"><div style="width:1.06in; height: 0.62in; margin:auto"><img style="width:100%; height:100%" src="images/702814d3.jpg"/></div></div><div class="footer"><div class="sim2 verde">Partilhar</div><div class="outros vermelho">Apagar</div></div>');
		$("#slick_galeria .caixa").removeClass("sem_fotos");
	} else {
		$("#slick_galeria").slick("slickAdd", '<div class="caixa"></div><div class="caixa galeria"><div class="header"><div class="hora"></div><div class="localizacao">Galeria</div></div><div class="corpo com_footer"><div style="width:1.06in; height: 0.62in; margin:auto"><img style="width:100%; height:100%" src="images/702814d3.jpg"/></div></div><div class="footer"><div class="sim2 verde">Partilhar</div><div class="outros vermelho">Apagar</div></div></div>');
	};
}

$.fn.rmPhoto = function(){
	if($("#slick_galeria .caixa").length == 1) {
		$("#slick_galeria .caixa").find(".corpo").replaceWith('<div class="corpo"><div class="txt">Não tem fotos</div></div>');
		$("#slick_galeria .caixa").addClass("sem_fotos");
	} else {
		if(remover != 0){ remover--; };
		$("#slick_galeria").slick("slickRemove", remover);
		if($("#slick_galeria .caixa").length != 0) $("#slick_galeria").slick("slickRemove", remover);
		$("#slick_galeria .caixa").css("width", "1.06in");
	};
	
	setTimeout(function(){remover = 0;}, 30);
}

$(document).ready(function(e){	
	$(".slick, .caixa, .button, #top").css("visibility", "hidden");
	setTimeout(function(){
		if(!mostrarTodos){ $(this).changeTab("#blackscreen"); setTimeout(function(){$(this).changeTab("#slick_lockscreen");}, 1); }
		setTimeout(function(){$(".slick, .caixa, .button, #top").css("visibility", "visible");}, 2);
		$("#slick_lockscreen").slick({
			infinite: false,
			arrows: false
		});
		$("#slick_principal, #slick_artistas, #slick_galeria, #slick_lista, #slick_toque, #slick_sugerir, #slick_ajuda_menus, #slick_ajuda_setas").slick({
			infinite: false,
			slidesToScroll: 2,
			nextArrow: '<div class="slick-next"></div>',
			prevArrow: '<div class="slick-prev"></div>'
		});
	}
	, 2000);

	$('#slick_lockscreen').on('swipe', function(event, slick, direction){
		setTimeout(function(){
			$(this).changeTab(unlock);
			unlock = 0;
			$("#slick_lockscreen").slick("slickGoTo", 0, true);
		}, 300);
	});
	
	$(".lista_precos li").click(function(e){
		$(this).changeTab("#nprodutos");
		$("#np").text($(this).attr("n"));
		produto = "#" + $(this).attr("id");
		if( $(".tt").text() < 0.1 ) 
			$("#nprodutos .sim").removeClass("verde");
		else 
			$("#nprodutos .sim").addClass("verde");
		$("#nprodutos .localizacao").text($(this).parent().parent().parent().find(".localizacao").text());
	});

	$(".pagamento, .entrega").click(function(e){
		$(".lista_final").find(("." + $(this).attr("class"))).remove();
		$(this).clone().prependTo(".lista_final");
	});
	
	$("#amigos_sugerir1 li").click(function(){
		if( $(this).hasClass("selected") ){
			$(this).removeClass("selected");
			setTimeout(function(){
				if($("#amigos_sugerir1 	.selected").length > 1){
					$("#amigos_sugerir1 .sim2").addClass("verde");}
				else{
					$("#amigos_sugerir1 .sim2").removeClass("verde");};
			}, 10);
		}
		else {
			$(this).addClass("selected");
			$(this).parent().parent().parent().parent().find(".sim2, .sim").addClass("verde");};
	});
	
	$("#amigos_sugerir2 li").click(function(){
		$(this).addClass("selected").siblings().removeClass("selected");
		$(this).parent().parent().parent().parent().find(".sim2, .sim").addClass("verde");
	});
	
	$("#mais").click(function(e){ 
		if($("#np").text() != 9999) $("#np").text(parseInt($("#np").text())+1);
		$("#nprodutos").find(".sim").addClass("verde");
		cancelar = true;
	});
	
	$("#menos").click(function(e){ 
		if($("#np").text() != 0) $("#np").text(parseInt($("#np").text())-1);
		if( (parseFloat($(".tt").text()) + ( parseFloat($(produto).eq(0).attr("price")) * (parseInt($("#np").text()) - parseInt($(produto).eq(0).attr("n"))) )) < 0.1) {
			$("#nprodutos .sim").removeClass("verde"); cancelar = false; }
	});

	$(".icon_camera").on("mousedown", function(){
		$(".icon_camera").css("background-color", "black");
		$(".icon_camera_button").css("background-color", "white");
		$(".icon_camera_body").css("background-color", "white");
		$(".icon_camera_cannon").css("background-color", "white");
		$(".icon_camera_cannon").css("border-color", "black");     
	});

	$(".icon_camera").on("mouseup", function(){
		$(".icon_camera").css("background-color", "#D8D8D8");      
		$(".icon_camera_button").css("background-color", "black");
		$(".icon_camera_body").css("background-color", "black");
		$(".icon_camera_cannon").css("background-color", "black");
		$(".icon_camera_cannon").css("border-color", "white");
		$(this).changeTab("#multimedia_camara1");
	});

	$("#multimedia_galeria ul").click(function(){ $(this).changeTab("#multimedia_foto"); });
	
	$(".onoffswitch").on("click", function(){
		if((($.now())-time) > 100){
			time = $.now();
			var image = $("#top img");
			if( image.attr("src") == 'images/cam+flash_ON.png'){
				image.attr("src", 'images/cam+flash.png');}
			else {
				image.attr("src", 'images/cam+flash_ON.png');}
		};
	});

	$(document.body).on("click", "#notificacoes li", function(){
		$(this).remove();
		if($("#notificacoes").attr("n") <= 1) {
			$("#top2").css("visibility", "hidden");
			$("#notificacoes ul").html("<li>Sem Alertas</li>");
			$("#notificacoes").attr("n", 0);
			$(".nalertas").css("visibility", "hidden");
		} else {
			$("#notificacoes").attr("n", parseInt($("#notificacoes").attr("n"))-1);
			if ($("#notificacoes").attr("n") < 10)
				$(".nalertas").css("visibility", "visible").text($("#notificacoes").attr("n"));
			else
				$(".nalertas").css("visibility", "visible").text("9+");
		}
	});

	$("#button_home").click(function(){
			if(cancelar){
				cancelar_nao="#"+$(".caixa:visible").attr("id");
				$(this).changeTab("#verificacao_cancelar");
				cancelar_sim = "#slick_principal";
			}
			else if($(".caixa:visible").eq(0).attr("id") != "lockscreen" && $(".caixa:visible").eq(0).attr("id") != "black_screen"){
				$(this).limpar(); $(this).changeTab("#slick_principal");
			}
	});

	$("#button_lock").click(function(){
		if(unlock == 0) {
			if($(".caixa:visible").hasClass("slick-slide")){
				unlock = "#"+$(".caixa:visible").parent().parent().parent().attr("id");}
			else{
				unlock = "#"+$(".caixa:visible").attr("id");};
			$(this).changeTab("#black_screen");
		} else if($(".caixa:visible").eq(0).attr("id") == "lockscreen"){
			$(this).changeTab("#black_screen");
		} else {
			$(this).changeTab("#slick_lockscreen");
		};
	});
	
	$("#button_help").click(function(){
		if($(".caixa:visible").eq(0).attr("id") != "lockscreen" && $(".caixa:visible").eq(0).attr("id") != "black_screen" && $(".caixa:visible").eq(0).attr("id") != "ajuda") {
			if($(".caixa:visible").hasClass("slick-slide")){
				ajuda = "#"+$(".caixa:visible").parent().parent().parent().attr("id");}
			else{
				ajuda = "#"+$(".caixa:visible").attr("id");};
			$(this).changeTab("#ajuda");
		};
	});

	$("body")
	.on("mousedown", ".sim, .sim2, li, .menu, #button_back, .nao, .outros", function(){time = $.now(); })
	.on("mouseup", ".sim, .sim2", function(){
		if((($.now())-time) < 150){
			if($(this).hasClass("verde") || $(this).hasClass("vermelho")){
				var caixa = $(this).parent().parent();
				if($(caixa).attr("id") == ("amigos_sugerir_verificação")){						$(caixa).changeTab("#amigos_sugerir0");}
				else if($(caixa).attr("id") == "amigos_adicionar1"){							$(caixa).changeTab("#amigos_adicionar2");}
				else if($(caixa).attr("id") == "amigos_adicionar2"){							aprox = 1; $(this).addFriend("Eduardo", "images/ed.gif"); $(this).changeTab("#conclusao"); conclusao_prefixo = "#amigos"; finishTxt("Amigo adicionado com sucesso!");}
				else if($(caixa).attr("id") == "amigos_adicionar3"){							$(this).changeTab("#amigos");}
				else if($(caixa).attr("id") == "amigos_sugerir0"){								$(caixa).changeTab("#amigos_sugerir1"); cancelar = true; cancelar_sim = "#amigos"; cancelar_nao="#amigos_sugerir1";}
				else if($(caixa).attr("id") == "amigos_sugerir1"){								$(caixa).changeTab("#amigos_sugerir2"); cancelar_nao="#amigos_sugerir2";}
				else if($(caixa).attr("id") == "amigos_sugerir2"){								$(caixa).changeTab("#slick_sugerir");  preferencias = true; cancelar = false;}
				else if($(caixa).attr("id") == "amigos_verificacao"){							setTimeout(function(){$(this).changeTab("#conclusao");},50); conclusao_prefixo = "#amigos"; finishTxt("Amigo removido com sucesso!"); $(this).rmFriend();}
				else if($(caixa).attr("id") == "multimedia_verificacao") {					$(this).changeTab("#conclusao"); conclusao_prefixo = "#slick_principal"; finishTxt("Foto removida com sucesso!"); $(this).rmPhoto(); }
				else if($(caixa).parent().parent().parent().attr("id") == "slick_lista") {		$(this).changeTab("#conclusao"); conclusao_prefixo = "#amigos"; finishTxt("Toque enviado com sucesso!");}	
				else if($(caixa).attr("id") == "mostra_diversoes_tempo"){						$(this).changeTab("#conclusao"); finish(val); $(this).limpar(); }
				else if($(caixa).attr("id") == "recibo"){ 										$(this).changeTab("#conclusao"); finish(val); }
				else if($(caixa).attr("id") == "verificacao_cancelar"){							$(this).changeTab(cancelar_sim); $(this).limpar();}
				else if($(caixa).parent().parent().parent().attr("id") == "slick_artistas") {	$(this).changeTab("#" + $(caixa).attr("id") + "_musica");}	
				else if($(caixa).parent().parent().parent().attr("id") == "slick_toque") {		$(this).changeTab("#conclusao"); conclusao_prefixo = "#amigos"; finishTxt("Toque enviado com sucesso!");}	
				else if(/^concertos_tocar_palco/.test($(caixa).attr("id"))){					$(this).changeTab("#concertos_tocar_palcos");}
				else if($(caixa).parent().parent().parent().attr("id") == "slick_galeria") {	$(this).changeTab("#multimedia_camara3");}
				else if($(caixa).hasClass("desconhecido")){	
					var n = $(caixa).find(".corpo div:first-child").text();
					var img = $(caixa).find("img").attr("src");
					var i = $("#slick_sugerir").slick('slickCurrentSlide'); if(i != 0) i--;
					$(this).changeTab("#slick_sugerir");
					$("#slick_sugerir").slick("slickRemove", i); 
					if($("#slick_sugerir .caixa").length > 0) $("#slick_sugerir").slick("slickRemove", i);
					else $("#slick_sugerir").slick("slickAdd", '<div class="caixa amigo"><div class="header"><div class="hora"></div><div class="localizacao">Sugerir</div></div><div class="corpo"><div class="txt">Neste momento, não existe ninguém que combine consigo</div></div></div>');
					$(this).addFriend(/(.+),/.exec(n)[1], img);
					setTimeout(function(){$(this).changeTab("#conclusao"); conclusao_prefixo = "#amigos"; finishTxt("Amigo adicionado com sucesso!");}, 50);
				}
				else if($(caixa).attr("id") == "multimedia_camara1"){
					$(this).addPhoto();
					$(this).changeTab("#conclusao");
					setTimeout(function(){$(this).changeTab("#multimedia_camara2");}, 1000);
				}
				else if($(caixa).attr("id") == "conclusao")	{
					if(conclusao_prefixo == "#comida")
						var c = "comida";
					else 
						var c = "diversoes";
					
					$(this).changeTab(conclusao_prefixo);
					if($(conclusao_prefixo + "_pedidos").attr("n") == 0) $(conclusao_prefixo + "_pedidos ul").html("");
					$(conclusao_prefixo + "_pedidos").attr("n", ($(conclusao_prefixo + "_pedidos").attr("n"))+1);
					$(conclusao_prefixo + "_pedidos .lista").append(('<li>' + val + ' <span origin="'+c+'" class="espera dir" info="'+ loja +'" tempo="' + (window.h * 100 + window.m + 5) + '">5 min</span></li>'));
					
					$(this).limpar(); val = Math.floor(Math.random()*10000 + 1);
				}
				else if($(caixa).attr("id") == "nprodutos")	{
					if(($("#np").text()) == 0){
						$(".lista_final").find(produto).remove();
						$(produto).removeClass("selected");
					} else if( $(".lista_final").find(produto).length == 0 ) {
						$(produto).clone()
							.find("span").text(Math.round( parseFloat($(produto).eq(0).attr("price")) * parseInt($("#np").text()) * 100 )/100 + "€")
							.parent().prepend("<span>" + ($("#np").text()) + "</span> ")
							.appendTo(".lista_final");
						$(produto).eq(0).addClass("selected");
					} else {
						$(".lista_final").find(produto).find("span:eq(0)").text($("#np").text());
						$(".lista_final").find(produto).find("span:eq(1)").text(Math.round( parseFloat($(produto).eq(0).attr("price")) * parseInt($("#np").text()) * 100 )/100 + "€");
					};
					
					$(".tt").text( 
						parseFloat($(".tt").text()) + 
						( parseFloat($(produto).eq(0).attr("price")) * (parseInt($("#np").text()) - parseInt($(produto).eq(0).attr("n"))) ) 
					).text((Math.round($(".tt").text() * 100)/100));
					
					$(produto).eq(0).attr("n", ($("#np").text()));
					
					$(this).changeTab("#verificacao");
				}
				else { $(caixa).foodManager(comida_prefixo, "sim");};
			};};
	})
	
	.on("mouseup", "li", function(){
		if((($.now())-time) < 150){
			var caixa = $(this).parent().parent().parent();
			if($(this).attr("id") == "comida_lojas_ze"){				$(this).changeTab("#lojas_ze"); comida_prefixo = "ze"; loja = "Bifas do Zé";}
			else if($(this).attr("id") == "comida_lojas_monica"){		$(this).changeTab("#lojas_monica"); comida_prefixo = "monica"; loja = "Tostas da Mónica";}
			else if($(this).attr("id") == "comida_lojas_pombo"){		$(this).changeTab("#lojas_pombo"); comida_prefixo = "pombo"; loja = "Pombo Social";}
			else if($(caixa).attr("id") == "entrega"){					$(this).changeTab("#pagamento");}
			else if($(caixa).attr("id") == "multimedia_galeria"){		$(this).changeTab("#multimedia_foto");}
			else if($(this).attr("class") == "multimedia_partilha"){	$(this).changeTab("#conclusao"); conclusao_prefixo = "#slick_principal"; finishTxt("Fotografia partilhada com sucesso!");}
			else if($(caixa).attr("id") == "pagamento"){				$("#recibo").show().siblings().hide();}
			else if($(this).attr("class") == "vai_ajuda_botoes")	   {$(this).changeTab("#ajuda_botoes");}
			else if($(this).attr("class") == "vai_ajuda_leds")	  	   {$(this).changeTab("#ajuda_leds");}
			else if($(this).attr("class") == "vai_ajuda_setas")		   {$(this).changeTab("#slick_ajuda_setas");}
			else if($(this).attr("class") == "vai_ajuda_menus")		   {$(this).changeTab("#slick_ajuda_menus");}
			else if($(this).attr("id") == "vai_concertos_tocar_palco0"){$(this).changeTab("#concertos_tocar_palco0"); concertos_historico = "#concertos_tocar_palco0";}
			else if($(this).attr("id") == "vai_concertos_tocar_palco1"){$(this).changeTab("#concertos_tocar_palco1"); concertos_historico = "#concertos_tocar_palco1";}
			else if($(this).attr("id") == "vai_concertos_tocar_palco2"){$(this).changeTab("#concertos_tocar_palco2"); concertos_historico = "#concertos_tocar_palco2";}
			else if($(this).attr("id") == "vai_concertos_cartaz_palco0"){$(this).changeTab("#concertos_cartaz_palco0");}
			else if($(this).attr("id") == "vai_concertos_cartaz_palco1"){$(this).changeTab("#concertos_cartaz_palco1");}
			else if($(this).attr("id") == "vai_concertos_cartaz_palco2"){$(this).changeTab("#concertos_cartaz_palco2");}
			else if($(this).attr("class") == "vai_diversoes_tempo"){	$(this).changeTab("#mostra_diversoes_tempo"); loja = $(this).text();}
			else if($(this).attr("class") == "vai_mapa_amigos"){		$(this).changeTab("#mostra_mapa_amigos");}
			else if($(this).attr("class") == "vai_mapa_palcos"){		$(this).changeTab("#mostra_mapa_palcos");}
			else if($(this).attr("class") == "vai_mapa_lojas"){			$(this).changeTab("#mostra_mapa_lojas");}
			else if($(this).attr("class") == "vai_mapa_diversoes"){		$(this).changeTab("#mostra_mapa_diversoes");}
			else if($(this).attr("id") == "vai_multimedia_camara"){		$(this).changeTab("#multimedia_camara");}
			else if($(this).attr("id") == "vai_multimedia_camara3"){	$(this).changeTab("#multimedia_camara3");}
			else if($(this).attr("id") == "vai_multimedia"){			$(this).changeTab("#slick_principal");}
			else if($(this).attr("id") == "verificacao_mais"){			$(this).changeTab("#"+comida_prefixo+"_encomendar");}
			else if($(this).attr("id") == "verificacao_fim"){			$(this).changeTab("#entrega");};
		};
	})
	
	.on("mouseup", ".menu", function(){
		if((($.now())-time) < 150){
			if($(this).attr("id") == "lojas_"+comida_prefixo+"_encomendar"){$(this).changeTab("#"+comida_prefixo+"_encomendar");}
			else if($(this).attr("id") == "lojas_"+comida_prefixo+"_direcoes"){$(this).changeTab("#"+comida_prefixo+"_direcoes");}
			else if($(this).attr("id") == "menu_amigos_lista"){				$(this).changeTab("#slick_lista");}
			else if($(this).attr("id") == "menu_amigos_adicionar"){			$(this).changeTab("#amigos_adicionar");}
			else if($(this).attr("id") == "menu_amigos_toque"){				$(this).changeTab("#slick_toque");}
			else if($(this).attr("id") == "menu_amigos_sugerir"){			
													if (preferencias)		$(this).changeTab("#amigos_sugerir_verificação");
													else {					$(this).changeTab("#amigos_sugerir0");};}
			else if($(this).attr("id") == "menu_concertos_artistas"){		$(this).changeTab("#slick_artistas");}
			else if($(this).attr("id") == "menu_concertos_tocar"){			$(this).changeTab("#concertos_tocar_palco0"); concertos_historico = "#concertos_tocar_palco0";}
			else if($(this).attr("id") == "menu_concertos_cartaz"){			$(this).changeTab("#concertos_cartaz");}
			else if($(this).attr("id") == "menu_concertos_lanterna"){		$(this).changeTab("#concertos_lanterna");}
			else if($(this).attr("id") == "menu_diversoes_filas"){			$(this).changeTab("#diversoes_filas");}
			else if($(this).attr("id") == "menu_diversoes_pedidos"){		$(this).changeTab("#diversoes_pedidos");}
			else if($(this).attr("id") == "menu_mapa_amigos"){				$(this).changeTab("#mapa_amigos");}
			else if($(this).attr("id") == "menu_mapa_palcos"){				$(this).changeTab("#mapa_palcos");}
			else if($(this).attr("id") == "menu_mapa_diversoes"){			$(this).changeTab("#mapa_diversoes");}
			else if($(this).attr("id") == "menu_mapa_lojas"){				$(this).changeTab("#mapa_lojas");}
			else if($(this).attr("id") == "menu_multimedia_camara"){		$(this).changeTab("#multimedia_camara");}
			else if($(this).attr("id") == "menu_multimedia_galeria"){		$(this).changeTab("#slick_galeria");}
			else if($(this).attr("id") == "menu_principal_ajuda"){			$(this).changeTab("#ajuda"); ajuda = "#slick_principal"; }
			else if($(this).attr("id") == "menu_amigos_adicionar"){			$(this).changeTab("#amigos_adicionar");}
			else if($(this).attr("id") == "menu_principal_amigos"){			$(this).changeTab("#amigos");}
			else if($(this).attr("id") == "menu_principal_concertos"){		$(this).changeTab("#concertos");}
			else if($(this).attr("id") == "menu_principal_diversoes"){		$(this).changeTab("#diversoes"); conclusao_prefixo='#diversoes';}
			else if($(this).attr("id") == "menu_principal_mapa"){			$(this).changeTab("#mapa");}
			else if($(this).attr("id") == "menu_principal_notificacoes"){	$(this).changeTab("#notificacoes");}
			else if($(this).attr("id") == "menu_principal_comida"){			$(this).changeTab("#comida"); conclusao_prefixo='#comida';}
			else if($(this).attr("id") == "menu_comida_lojas"){				$(this).changeTab("#comida_lojas");}
			else if($(this).attr("id") == "menu_comida_pedidos"){			$(this).changeTab("#comida_pedidos");}
			else { $(this).foodManager(comida_prefixo, "menu");};
		};
	})
	
	.on("mouseup", "#button_back, .nao", function(){
		if((($.now())-time) < 150){
			if($(this).attr("id") == "button_back" || ( $(this).hasClass("nao") &&  ($(this).hasClass("vermelho") || $(this).hasClass("verde")))){
				var visible = $(".caixa:visible");	
				if(visible.attr("id") == "comida_lojas"){ 					$(this).changeTab("#comida");}
				else if($(".caixa[id^='concertos_palco'][id$='_musica']").is(":visible")){$(this).changeTab("#slick_artistas");}
				else if(visible.attr("id") == "comida_pedidos"){ 			$(this).changeTab("#comida");}
				else if(visible.attr("id") == "ajuda"){						$(this).changeTab(ajuda);}
				else if(visible.attr("id") == "ajuda_botoes")		   {$(this).changeTab("#ajuda");}
				else if(visible.attr("id") == "ajuda_menus")		   {$(this).changeTab("#ajuda");}
				else if(visible.attr("id") == "ajuda_setas")		   {$(this).changeTab("#ajuda");}
				else if(visible.attr("id") == "ajuda_leds")		   	   {$(this).changeTab("#ajuda");}
				else if(visible.attr("id") == "amigos"){					$(this).changeTab("#slick_principal");}
				else if(/^amigos_adicionar/.test(visible.attr("id"))){		$(this).changeTab("#amigos");}
				else if(visible.attr("id") == "amigos_sugerir_verificação"){$(this).changeTab("#slick_sugerir");}
				else if(visible.attr("id") == "amigos_sugerir0"){			$(this).changeTab("#amigos");}
				else if(visible.attr("id") == "amigos_sugerir1"){			$(this).changeTab("#verificacao_cancelar"); }
				else if(visible.attr("id") == "amigos_sugerir2"){			$(this).changeTab("#verificacao_cancelar");}
				else if(visible.eq(0).hasClass("amigo")){					$(this).changeTab("#amigos");}
				else if(visible.eq(0).hasClass("desconhecido")){			$(this).changeTab("#amigos");}
				else if(visible.attr("id") == "comida"){					$(this).changeTab("#slick_principal");}
				else if(visible.attr("id") == "concertos"){					$(this).changeTab("#slick_principal");}
				else if(visible.eq(0).attr("id") == "concertos_palco00"){	$(this).changeTab("#concertos");}
				else if(visible.attr("id") == "concertos_tocar_palcos"){	$(this).changeTab(concertos_historico);}
				else if(/^concertos_tocar_palco/.test(visible.attr("id"))){	$(this).changeTab("#concertos");}
				else if(visible.attr("id") == "concertos_cartaz"){			$(this).changeTab("#concertos");}
				else if(/^concertos_cartaz_palco/.test(visible.attr("id"))){$(this).changeTab("#concertos_cartaz");}
				else if(visible.attr("id") == "conclusao"){		 			$(this).changeTab(conclusao_prefixo); $(this).limpar(); }
				else if(visible.attr("id") == "diversoes"){					$(this).changeTab("#slick_principal");}
				else if(visible.attr("id") == "diversoes_filas"){			$(this).changeTab("#diversoes");}
				else if(visible.attr("id") == "diversoes_pedidos"){			$(this).changeTab("#diversoes");}
				else if(visible.attr("id") == "entrega"){ 					$(this).changeTab("#"+comida_prefixo+"_encomendar");}
				else if(visible.attr("id") == "lojas_"+comida_prefixo){ 	$(this).changeTab("#comida_lojas");}
				else if(visible.attr("id") == "concertos_lanterna"){		$(this).changeTab("#concertos");}
				else if(visible.attr("id") == "mapa"){						$(this).changeTab("#slick_principal");}
				else if(visible.attr("id") == "mapa_amigos"){				$(this).changeTab("#mapa");}
				else if(visible.attr("id") == "mapa_diversoes"){			$(this).changeTab("#mapa");}
				else if(visible.attr("id") == "mapa_palcos"){				$(this).changeTab("#mapa");}
				else if(visible.attr("id") == "mapa_lojas"){				$(this).changeTab("#mapa");}
				else if(visible.attr("id") == "mostra_mapa_amigos"){		$(this).changeTab("#mapa_amigos");}
				else if(visible.attr("id") == "mostra_mapa_lojas"){			$(this).changeTab("#mapa_lojas");}
				else if(visible.attr("id") == "mostra_mapa_palcos"){		$(this).changeTab("#mapa_palcos");}
				else if(visible.attr("id") == "mostra_mapa_diversoes"){		$(this).changeTab("#mapa_diversoes");}
				else if(visible.attr("id") == "mostra_diversoes_tempo"){	$(this).changeTab("#diversoes_filas");}
				else if(visible.eq(0).hasClass("galeria")){		 			$(this).changeTab("#slick_principal");}
				else if(visible.attr("id") == "multimedia_verificacao"){	$(this).changeTab("#slick_galeria");}
				else if(visible.attr("id") == "multimedia_camara"){			$(this).changeTab("#slick_principal");}
				else if(visible.attr("id") == "multimedia_camara1"){		$(this).changeTab("#multimedia_camara");}
				else if(visible.attr("id") == "multimedia_camara2"){		$(this).changeTab("#slick_principal");}
				else if(visible.attr("id") == "multimedia_camara3"){		$(this).changeTab("#slick_principal");}
				else if(visible.eq(0).hasClass("galeria")){					$(this).changeTab("#slick_principal");}
				else if(visible.attr("id") == "multimedia_foto"){			$(this).changeTab("#multimedia_galeria");}
				else if(visible.attr("id") == "notificacoes"){				$(this).changeTab("#slick_principal");}
				else if(visible.eq(0).attr("id") == "principal1"){		 	$(this).changeTab("#slick_principal");}
				else if(visible.attr("id") == "pagamento"){ 				$(this).changeTab("#entrega");}
				else if(visible.attr("id") == "recibo"){ 					$(this).changeTab("#pagamento");}
				else if(visible.attr("id") == "nprodutos"){					$(this).changeTab("#"+comida_prefixo+"_encomendar");}
				else if(visible.attr("id") == "verificacao"){				$(this).changeTab("#"+comida_prefixo+"_encomendar");}
				else if(visible.attr("id") == "verificacao_cancelar"){		$(this).changeTab(cancelar_nao);}
				else if(visible.attr("id") == comida_prefixo+"_direcoes"){	$(this).changeTab("#lojas_"+comida_prefixo);}
				else if(visible.attr("id") == comida_prefixo+"_encomendar"){if(cancelar){ $(this).changeTab("#verificacao_cancelar"); cancelar_nao = "#"+comida_prefixo+"_encomendar"; cancelar_sim="#lojas_"+comida_prefixo;}
																			else 		  $(this).changeTab("#lojas_"+comida_prefixo);}
				else { visible.foodManager(comida_prefixo, "nao");};
		};};
	})
	.on("mouseup", ".outros", function(){
		if((($.now())-time) < 150){
			var caixa = $(this).parent().parent();
			if($(caixa).attr("id") == "amigos_verificacao"){							$(this).changeTab("#slick_lista"); $("#slick_lista").slick("slickGoTo", remover, true); }
			else if($(caixa).parent().parent().parent().attr("id") == "slick_galeria"){ $(this).changeTab("#multimedia_verificacao"); remover = $('#slick_galeria').slick('slickCurrentSlide');}
			else if($(caixa).parent().parent().parent().attr("id") == "slick_lista") {	$(this).changeTab("#amigos_verificacao"); remover = $('#slick_lista').slick('slickCurrentSlide'); };
		};
	});
	
	shortcut.add("Q", function(){
		goToFuture(60);
	});

	shortcut.add("W", function(){
		goToFuture(20);
	});

	shortcut.add("E", function(){
		var tmp = true;
		$("#slick_lista .corpo div:first-child").each(function(){
			if($(this).text() == "Ana Malhoa"){
				toque();
				tmp = false;
			}
		});
		if(tmp) 
			alert('Já não tem "Ana Malhoa" como amiga!');
	});
	
	shortcut.add("T", function(e){
		var tmp = true;
		$("#slick_lista .corpo div:first-child").each(function(){
			if($(this).text() == "Carolina")
				tmp = false;
		});
		
		if(tmp) {
			$(this).addFriend("Carolina", "images/carol.gif");
			if($("#notificacoes").attr("n") == 0) $("#notificacoes ul").html("");
			$("#notificacoes").attr("n", parseInt($("#notificacoes").attr("n"))+1);
			$("#notificacoes ul").append("<li>Carolina adiciono-o</li>");
			if ($("#notificacoes").attr("n") < 10)
				$(".nalertas").css("visibility", "visible").text($("#notificacoes").attr("n"));
			else
				$(".nalertas").css("visibility", "visible").text("9+");
			$("#top2").css("visibility", "visible");
		}
		else {
			alert('Ja tem "Carolina" como amiga!');
		};
	});
	
	shortcut.add("R", function(){
		if($(".caixa:visible").attr("id") == "amigos_adicionar"){
			if(aprox == 0){
				$(this).changeTab("#amigos_adicionar2");
			} else {
				$("#slick_lista .corpo div:first-child").each(function(){
					if($(this).text() == "Eduardo")
						aprox = 2;
				});
				if(aprox == 1)
					$(this).changeTab("#amigos_adicionar1");
				else
					$(this).changeTab("#amigos_adicionar3");
				aprox = 1;
			}
		}
	});
});
