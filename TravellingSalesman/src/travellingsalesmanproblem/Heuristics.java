package travellingsalesmanproblem;

public class Heuristics extends Graphs {

    private final int vertices;    
    private final int[][] caminho;
    private final int[] caminho2;
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
        caminho2 = new int[vertices];
        
        for(int i = 0; i < vertices; i++) {
            for(int j = 0; j < vertices; j++) {
                caminho[i][j] = 0;
            }
        }
    }

    private void NearestNeighbor(int origem, int verticeInicial, int pos) {
        int peso = Integer.MAX_VALUE;
        int proxVertice = verticeInicial;
        caminho2[pos] = verticeInicial;
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
//            caminho[verticeInicial][proxVertice] = super.getPeso(verticeInicial, proxVertice);            
//            caminho[proxVertice][verticeInicial] = super.getPeso(verticeInicial, proxVertice);
            NearestNeighbor(origem, proxVertice, ++pos);
        } else {
            custoTotal += super.getPeso(verticeInicial, origem);
//            caminho[verticeInicial][origem] = super.getPeso(verticeInicial, origem);            
//            caminho[origem][verticeInicial] = super.getPeso(verticeInicial, origem);
            verticePai[origem] = verticeInicial;
        }
    }
    
    public void initNearestNeighbor(int cidadeInicial) {
        for (int i = 0; i < vertices; i++) {
            verticeConhecido[i] = 0;
            verticePai[i] = -1;
        }
        custoTotal = 0;

        NearestNeighbor(cidadeInicial, cidadeInicial, 0);
    }
    
    
//    private void h2OPT(int cidadeInicial){
//        for(int i = 0; i < verticePai.length; i++) {
//            for(int j = 0; j < i; j++) {
//                if (caminho[i][j] != 0) { // encontra o primeiro vertice para tentar a troca
//                    for(int k = i+1; k < verticePai.length; k++) {
//                        for(int m = 0; m < k; m++) {
//                            if (caminho[k][m] != 0 && m != i && m != j && k != j) {
//                                int novoPeso = ((super.getPeso(i, j) + super.getPeso(k, m)) - (super.getPeso(i, k) + super.getPeso(j, m)));
//                                if (novoPeso > 0) {
//                                    caminho[i][j] = 0;
//                                    caminho[j][i] = 0;
//                                    caminho[k][m] = 0;
//                                    caminho[m][k] = 0;
//                                    caminho[i][k] = super.getPeso(i, k);
//                                    caminho[k][i] = super.getPeso(i, k);
//                                    caminho[j][m] = super.getPeso(j, m);
//                                    caminho[m][j] = super.getPeso(j, m);
//                                    custoTotal -= novoPeso;
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }        
//    }
    
    private void h2OPT(int cidadeInicial){
        for(int i = 0; i < verticePai.length; i++) {
            for(int j = i+2; j < verticePai.length-1; j++) {
                if (caminho2[i] != caminho2[j] && caminho2[i] != caminho2[j+1] && caminho2[i+1] != caminho2[j] && caminho2[i+1] != caminho2[j+1]) {
                    int novoPeso = ((super.getPeso(caminho2[i], caminho2[i+1]) + super.getPeso(caminho2[j], caminho2[j+1])) - (super.getPeso(caminho2[i], caminho2[j]) + super.getPeso(caminho2[i+1], caminho2[j+1])));
                    // verifica se o peso apos a troca e vantajoso
                    if (novoPeso > 0) {
                        int aux = caminho2[i+1];
                        caminho2[i+1] = caminho2[j];
                        caminho2[j] = aux;
                        custoTotal -= novoPeso;
                        // arruma o vetor caminho apos a troca
                        int m = j-1;
                        for (int k = i+2; k != m && k < m ; k++) {
                            int aux2 = caminho2[k];
                            caminho2[k] = caminho2[m];
                            caminho2[m] = aux2;
                            m--;
                        }
                    }
                }
            }
        }        
    }
    
    public int[] init2OPT(int cidadeInicial) {
        for (int i = 0; i < vertices; i++) {
            verticeConhecido[i] = 0;
            verticePai[i] = -1;
        }
        custoTotal = 0;

        NearestNeighbor(cidadeInicial, cidadeInicial, 0);
        h2OPT(cidadeInicial);
        
        return this.getCaminho2();
    }

    public int[] getVerticePai() {
        return verticePai;
    }
    
    public int getCustoTotal() {
        return custoTotal;
    }

    public int[] getCaminho2() {
        return caminho2;
    }
    
    public int[][] getCaminho() {
        return caminho;
    }
    
}
