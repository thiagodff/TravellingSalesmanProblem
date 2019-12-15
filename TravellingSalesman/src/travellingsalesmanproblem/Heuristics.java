package travellingsalesmanproblem;

public class Heuristics extends Graphs {

    private final int vertices;    
    private final int[][] caminho;
    private final int[] verticeConhecido;
    private final int[] verticePai;
    private int custoTotal;

    public Heuristics(int vertices) {
        super(vertices);
        this.vertices = vertices;
        verticeConhecido = new int[vertices];
        verticePai = new int[vertices];
        custoTotal = 0;
        caminho = new int[vertices][vertices];
        
        for(int i = 0; i < vertices; i++) {
            for(int j = 0; j < vertices; j++) {
                caminho[i][j] = 0;
            }
        }
    }

    private void NearestNeighbor(int origem, int verticeInicial) {
        int peso = Integer.MAX_VALUE;
        int proxVertice = verticeInicial;
        verticeConhecido[verticeInicial] = 1;
        for (int i = 0; i < vertices; i++) {
            if (peso > super.getPeso(verticeInicial, i) && i != verticeInicial && verticeConhecido[i] == 0) {
                proxVertice = i;
                peso = super.getPeso(verticeInicial, i);
            }
        }
        
        if (proxVertice != verticeInicial) {
            verticePai[proxVertice] = verticeInicial;
            custoTotal += super.getPeso(verticeInicial, proxVertice);
            caminho[verticeInicial][proxVertice] = super.getPeso(verticeInicial, proxVertice);            
            caminho[proxVertice][verticeInicial] = super.getPeso(verticeInicial, proxVertice);
            NearestNeighbor(origem, proxVertice);
        } else {
            custoTotal += super.getPeso(verticeInicial, origem);
            caminho[verticeInicial][origem] = super.getPeso(verticeInicial, origem);            
            caminho[origem][verticeInicial] = super.getPeso(verticeInicial, origem);
            verticePai[origem] = verticeInicial;
        }
    }
    
    public void initNearestNeighbor(int cidadeInicial) {
        for (int i = 0; i < vertices; i++) {
            verticeConhecido[i] = 0;
            verticePai[i] = -1;
        }
        custoTotal = 0;

        NearestNeighbor(cidadeInicial, cidadeInicial);
    }
    
    // Refaz o vetor verticePai a partir da matrizAdj(caminhos)
    private void refazPai(int cidadeInicial){
        for(int i = 0; i < verticePai.length; i++) {
            int cont = 0;
            for(int j = 0; j < i; j++) {
                if (caminho[i][j] != 0) {
                    if (cont == 0) {
                        verticePai[i] = j;
                    }else if (cont == 1){
                        verticePai[verticePai[i]] = i;
                        verticePai[i] = j;
                    }
                    cont++;
                }
            }
        }
    }
    
    private void h2OPT(int cidadeInicial){
        for(int i = 0; i < verticePai.length; i++) {
            for(int j = 0; j < i; j++) {
                if (caminho[i][j] != 0) { // encontra o primeiro vertice para tentar a troca
                    for(int k = i+1; k < verticePai.length; k++) {
                        for(int m = 0; m < k; m++) {
                            if (caminho[k][m] != 0 && m != i && m != j && k != j) {
                                int novoPeso = ((super.getPeso(i, j) + super.getPeso(k, m)) - (super.getPeso(i, k) + super.getPeso(j, m)));
                                if (novoPeso > 0) {
                                    caminho[i][j] = 0;
                                    caminho[j][i] = 0;
                                    caminho[k][m] = 0;
                                    caminho[m][k] = 0;
                                    caminho[i][k] = super.getPeso(i, k);
                                    caminho[k][i] = super.getPeso(i, k);
                                    caminho[j][m] = super.getPeso(j, m);
                                    caminho[m][j] = super.getPeso(j, m);
                                    custoTotal -= novoPeso;
                                }
                            }
                        }
                    }
                }
            }
        }        
    }
    
//              if (i != verticePai[j] && verticePai[i] != j) {
//                    int peso2 = super.getPeso(j, verticePai[j]);
//                    if ((peso1 + peso2 - super.getPeso(i, j) - super.getPeso(verticePai[i], verticePai[j])) > 0) {
//                        // se o novo peso da troca for menor que o anterior
//                        // zera os vertices anteriores
//                        caminho[i][verticePai[i]] = 0;
//                        caminho[verticePai[i]][i] = 0;
//                        caminho[j][verticePai[j]] = 0;
//                        caminho[verticePai[j]][j] = 0;
//                        // e coloca os novos vertices
//                        caminho[i][j] = super.getPeso(i, j);
//                        caminho[j][i] = super.getPeso(i, j);
//                        caminho[verticePai[i]][verticePai[j]] = super.getPeso(verticePai[i], verticePai[j]);
//                        caminho[verticePai[j]][verticePai[i]] = super.getPeso(verticePai[i], verticePai[j]);
//                        // refaz o vertice pai a partir das novas conexoes da matriz de caminho
//                        refazPai(cidadeInicial);
//                        // atualiza o custo total
//                        custoTotal = super.getPeso(i, j) + super.getPeso(verticePai[i], verticePai[j]) - peso1 - peso2;
//                    }
//                }
    
    public void init2OPT(int cidadeInicial) {
        for (int i = 0; i < vertices; i++) {
            verticeConhecido[i] = 0;
            verticePai[i] = -1;
        }
        custoTotal = 0;

        NearestNeighbor(cidadeInicial, cidadeInicial);
        h2OPT(cidadeInicial);
    }

    public int[] getVerticePai() {
        return verticePai;
    }
    
    public int getCustoTotal() {
        return custoTotal;
    }
    
    public int[][] getCaminho() {
        return caminho;
    }
    
}
