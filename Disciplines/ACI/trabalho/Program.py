
def Main():
    Stack = []
    while(True):
        String = ReceberInput()

        #verificar erros
        #split da string por partes, separadas por espacos
        StringSplit = String.split(" ")

        if(len(StringSplit) == 1):
            if(CalculoPorPartesContemErros(StringSplit, Stack)):
                print "Stack demasiado pequena para realizar operacoes necessarias"
                continue
        if(len(StringSplit) > 1):
            if(CalculoSequencialContemErros(StringSplit, Stack)):
                print "Stack demasiado pequena para realizar operacoes necessarias"
                continue

        #nao foram encontrados erros, realizar operacoes
        RealizarOperacoes(StringSplit, Stack)


def ReceberInput():
    String = raw_input()
    return String


def RealizarOperacoes(String, Stack):
    
    for i in range(len(String)):
            valor = String[i]
            "verificacao de valor"
            "verificar se e um numero"
            try:
                v = int(valor)
                Stack.append( int(v))
               
            except:
                "nao e um numero"
                if(valor == '+'):
                    Somar(Stack)
                    
                elif(valor == '-'):
                    Subtrair(Stack)
                    
                elif(valor == 'clear'):
                    Clear(Stack)
    MostrarStack(Stack)      


def CalculoPorPartesContemErros(String, Stack):

    try:
        v = int(String[0])
        return False
    except:
        if(String[0] == '+'):
            return (len(Stack) < 2)
        if(String[0] == '-'):
            return (len(Stack) < 2)
        if(String[0] == 'clear'):
            return False

    return True  

def CalculoSequencialContemErros(String, Stack):
    "atribui-se um peso a cada operacao e no final verifica-se o resultado"
    resultado = 0
    nonoperation = 0
    for i in range(len(String)):
        valor = String[i]
        try:
            v = int(valor)
            resultado += 1
        except:
            "nao e um numero"
            if(valor == '+'):
                 resultado -= 1
            elif(valor == '-'):
                 resultado -= 1  
            elif(valor == 'clear'):
                 nonoperation += 1
                 resultado += 1
            else:
                return True
    #resultado < nonoperation: casos em que so existe clears duplos etc ou clears etc com operacoes
    #resultado <= 0, casos em que so existe operacoes sem clears etc.
    if(resultado <= nonoperation or resultado <= 0):
        return True

    return False


def MostrarStack(Stack):
    print Stack

def Somar(Stack):
    resultado = Stack[-2] + Stack[-1]
    PosicaoArray = len(Stack)-1
    Stack.pop(PosicaoArray)
    Stack[-1] = resultado

def Subtrair(Stack):
    resultado = Stack[-2] - Stack[-1]
    PosicaoArray = len(Stack)-1
    Stack.pop(PosicaoArray)
    Stack[-1] = resultado

def Clear(Stack):
   del Stack[:]

Main()