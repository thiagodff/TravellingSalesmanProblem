package travellingsalesmanproblem;

public class Heuristics extends Graphs {

    private final int vertices;
    private final int[] caminho;
    private final int[] verticeConhecido;
    private final int[] verticePai;
    private int custoTotal;
    private int custoTotalVerifica;

    public Heuristics(int vertices) {
        super(vertices);
        this.vertices = vertices;
        verticeConhecido = new int[vertices];
        verticePai = new int[vertices];
        custoTotal = 0;
        custoTotalVerifica = 0;
        caminho = new int[vertices];
    }

    private void NearestNeighbor(int origem, int verticeInicial, int pos, int[][] matrizAdj) {
        int peso = Integer.MAX_VALUE;
        int proxVertice = verticeInicial;
        caminho[pos] = verticeInicial;
        verticeConhecido[verticeInicial] = 1;
        for (int i = 0; i < vertices; i++) {
            if (peso > matrizAdj[verticeInicial][i] && i != verticeInicial && verticeConhecido[i] == 0) {
                proxVertice = i;
                peso = matrizAdj[verticeInicial][i];
            }
        }

        if (proxVertice != verticeInicial) {
            verticePai[proxVertice] = verticeInicial;
            custoTotal += matrizAdj[verticeInicial][proxVertice];
            NearestNeighbor(origem, proxVertice, ++pos, matrizAdj);
        } else {
            custoTotal += matrizAdj[verticeInicial][origem];
            verticePai[origem] = verticeInicial;
        }
    }

    public int[] initNearestNeighbor(int cidadeInicial, int[][] matrizAdj) {
        for (int i = 0; i < vertices; i++) {
            verticeConhecido[i] = 0;
            verticePai[i] = -1;
        }
        custoTotal = 0;

        NearestNeighbor(cidadeInicial, cidadeInicial, 0, matrizAdj);

        return this.getCaminho();
    }

    private void h2OPT(int cidadeInicial,int[][] matrizAdj, int[] caminho2) {
        for (int i = 0; i < verticePai.length; i++) {
            for (int j = i + 2; j < verticePai.length - 1; j++) {
                if (caminho2[i] != caminho2[j] && caminho2[i] != caminho2[j + 1] && caminho2[i + 1] != caminho2[j] && caminho2[i + 1] != caminho2[j + 1]) {
                    int novoPeso = ((matrizAdj[caminho2[i]][caminho2[i + 1]] + matrizAdj[caminho2[j]][caminho2[j + 1]]) - (matrizAdj[caminho2[i]][caminho2[j]] + matrizAdj[caminho2[i + 1]][caminho2[j + 1]]));
                    // verifica se o peso apos a troca e vantajoso
                    if (novoPeso > 0) {
                        int aux = caminho2[i + 1];
                        caminho2[i + 1] = caminho2[j];
                        caminho2[j] = aux;
                        custoTotal -= novoPeso;
                        // arruma o vetor caminho apos a troca
                        int m = j - 1;
                        for (int k = i + 2; k != m && k < m; k++) {
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

    public int[] init2OPT(int cidadeInicial, int[][]matrizAdj, int[] solucao) {
        for (int i = 0; i < vertices; i++) {
            verticeConhecido[i] = 0;
            verticePai[i] = -1;
        }

        h2OPT(cidadeInicial, matrizAdj, solucao);

        int i;
        for (i = 0; i < solucao.length - 1; i++) {
            custoTotalVerifica += super.getPeso(solucao[i], solucao[i+1]);
        }
        custoTotalVerifica += super.getPeso(solucao[i], solucao[0]);
        
        return solucao;
    }

    public int[] getVerticePai() {
        return verticePai;
    }

    public int getCustoTotal() {
        return custoTotal;
    }

    public int[] getCaminho() {
        return caminho;
    }

    public int getCustoTotalVerifica() {
        return custoTotalVerifica;
    }

}
