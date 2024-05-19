/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

SOBRE O FUNCIONAMENTO DO JSONReader:

Não é necessário escrever os objetos na ordem em que estão os valores no construtor, a função .get-tipo-() do JSONObject
procura pelo label da variável e então o JSONReader se encarrega de construir o objeto, isso funciona também para a cons-
trução das subclasses de move, existe um switch que lê o tipo de move no json e chama o construtor daquele move.

LEMBRE-SE DE ESCREVER AS LABELS EXATAMENTE COMO ESTÃO SENDO PROCURADAS NO .get<Tipo>("NomeDaLabel") DO JSONObject

Ex. de Move:

	{ 
		"Id": 7,
  		"Name": "Fire Punch",
  		"Category": "PHYSICAL",
  		"BasePower": 75,
  		"Accuracy": 100,
  		"Subclass": "DmgPlusFx",
    	"FxChance": 30,
    	"FxType": "BURN"
	}

Cada objeto no json é separado por uma vírgula depois dos colchetes.

TOME CUIDADO PARA NÃO COLOCAR UM OBJETO DENTRO DE OUTRO

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////