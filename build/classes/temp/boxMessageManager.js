/*Create by LuisAraujo - 2018
*Code on Creative Commons Licenses (CC BY 4.0) https://creativecommons.org/licenses/by/4.0/
*/

//Classe BBMManager
BMManager = function(){
	//chama o start quando for carregado
	$("#body").ready( this.start );
	
	//armazena o id do objeto atual
	current_target = "";
	box_is_showing = false;
	
	arrayMessages = [];
	
}


BMManager.prototype.start = function(){
	
	
	/*cadastra na lista de evento o evento do movimento do mouse
	*caso o span seja usado para outra coisa, considere adicionar uma classe para todos os elementos em destaque
	*Ex: <span class='element' ></span>
	*Aqui faça: $(".element")
	*/
	
	$("span")
		.mouseover(function( event ){
		
			//salvando o id capturado
			//bmm.current_target = event.target.id;
			bmm.current_target = $(this).parent()[0].id;
			
			console.log(bmm.current_target)
			/*
			* chamar a função após x milissegundo
			* obs:
			* Isso é opcional, adiciona a ideia de que o usuário deve repousar o mouse sobre o
			* elemento, se quiser que seja imediamentamente basta:
			* this.showBox(event.target.id)
			*/
			
			
			setTimeout( function(){
				bmm.showBox($(event.target).parent()[0].id, event.target.offsetTop,  event.target.offsetLeft )
				}, 100);
			
	
		})	

		.mouseout(function( event ){	
	
			if(bmm.box_is_showing){
				bmm.hideBox();
			}
		});
		
		/*Trando possivel erro
		* Eventos de mouse são sempre chatos, pode ocorrer do navegador não capitura algum evento e deixar o box na tela, então 
		* verificamos quando o mouse se move; 
		*/
		$("#body").mousemove(function( event ){	
			
			if(bmm.box_is_showing){
				if(event.target.tagName != "SPAN"){
					if(event.target.tagName != "A"){
						
						if(bmm.current_target != event.target.id){
							console.log(event.target.tagName);
							bmm.hideBox();
						}
					}else if( (event.target.id != "bm") && (event.target.id != "bm-child")  ){
						
						bmm.hideBox();
					}
						
				}else if(bmm.current_target != $(event.target).parent()[0].id ){
				
					bmm.hideBox();
				}
			}
		});
		

}

/*
* Função para exibição do Box de Mensagem
*/
BMManager.prototype.showBox = function(id_expected, top_pos, left_pos){
	
	//if not have change on target 
	if(this.current_target == id_expected){
		$("#bm").show();
		
		//pode cousar erros em elementos sem id
		try {
			
			//converte a string em json
			jsonMessage = JSON.parse(arrayMessages[ parseInt(id_expected) ]);
		
			$("#bm-title").text(jsonMessage.title)

			$("#bm-monossilabo").text(jsonMessage.monossilabo) 
			$("#bm-dissilabo").text(jsonMessage.dissilabo)
			$("#bm-tetrassilabo").text(jsonMessage.tetrassilabo)
			$("#bm-pentassilabo").text(jsonMessage.pentassilabo)
			$("#bm-hexassilabo").text(jsonMessage.hexassilabo)
			$("#bm-heptassilabo").text(jsonMessage.heptassilabo)
			$("#bm-octossilabo").text(jsonMessage.octossilabo)
			$("#bm-eneassilabo").text(jsonMessage.eneassilabo)
			$("#bm-decassilabo").text(jsonMessage.decassilabo) 
			$("#bm-hendecassilabo").text(jsonMessage.hendecassilabo) 
			$("#bm-dodecassilabo").text(jsonMessage.dodecassilabo); 
		}
		catch(err) {
			console.error("Element without id");
		}
		
		//colocando o box na posição - próximo à frase
		
		$("#bm").css("top", top_pos+30);
		$("#bm").css("left", left_pos-20);
		
		
		//flag para controlar o box (pode usar também .css("display") == block .. )
		bmm.box_is_showing = true;

	}
	
}


/*
* Função para esconder o Box de Mensagem
*/
BMManager.prototype.hideBox = function(id_expected){
	
	$("#bm-title").text("");
	$("#bm-monossilabo").text("");
	$("#bm-dissilabo").text("");
    $("#bm-tetrassilabo").text("");
	$("#bm-pentassilabo").text("");
	$("#bm-hexassilabo").text("");
	$("#bm-heptassilabo").text("");
	$("#bm-octossilabo").text("");
	$("#bm-eneassilabo").text("");
	$("#bm-decassilabo").text("");
	$("#bm-hendecassilabo").text("");
	$("#bm-dodecassilabo").text("");
	$("#bm").hide();
	
	bmm.box_is_showing = false;
}



//Instanciando o objeto
bmm = new BMManager();