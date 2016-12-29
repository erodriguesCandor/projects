#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <limits.h>
 
/**/
typedef struct graph Graph;
typedef struct edge Edge;
typedef struct node *link;

struct node{
	int v;
	link next;
};

struct edge{
    int from, to, weight;
};
 

struct graph
{

    int V, E; 
    link* adj;
    Edge* edge;
};


/*Global Variable*/ 
int *queue;
int init, end;

/*New Node*/
link New_Node(int v, link next){
	link x = malloc(sizeof *x);
	x->v = v;
	x->next = next;
	return x;
}

/*Graph Init*/
Graph* graph_Init(int V, int E)
{
    int i;
    Graph* graph = (Graph*)malloc(sizeof (Graph));
    graph->V = V;
    graph->E = E; 
    graph->edge =(Edge*) malloc((graph->E+1) * sizeof(Edge) );
    graph->adj =(link*) malloc((graph->V+1) * sizeof(link) );
    for(i=0; i<V+1;i++) graph->adj[i]=NULL; 
    return graph;
}
 
/*Output*/
void printDistancias(int dist[],int pi[], int n)
{
    int i;
    for (i = 0; i < n; ++i){
	if( dist[i] == INT_MAX)
		printf("U\n");
	if( dist[i] == INT_MIN)
		printf("I\n");
	if( dist[i] != INT_MAX && dist[i] != INT_MIN)
        printf("%d\n",dist[i]);
}
}
 

/* ----------QUEUE INIT----------*/
void QUEUEinit(int maxN)
{ 
  queue = (int*) malloc(maxN*sizeof(int)); 
  init = 0; 
  end = 0; 
}

int QUEUEempty()
{ 
  return init == end; 
}

void QUEUEput(int i)
{ 
  queue[end++] = i; 
}

int QUEUEget()
{ 
  return queue[init++]; 
}

void QUEUEfree()
{
  free(queue);
}
/* ----------QUEUE END----------*/


/*Bellman Ford and Negative Cycle*/
void BellmanFordandNegativeCycle(Graph* graph, int from)
{
    int i,j,u,v,weight,w, altera=1;
    int V = graph->V;
    int E = graph->E;
    int dist[V], pi[V];
    link x;
    QUEUEinit(V+V); 

    for (i = 0; i < V; i++){
        dist[i]   = INT_MAX;
	pi[i] = INT_MIN;
    };
    dist[from] = 0;
 
    for (i = 1; i <= V-1; i++)
    {
	if(altera==1){ altera=0;
        for (j = 0; j < E; j++)
        {
            u = graph->edge[j].from;
            v = graph->edge[j].to;
            weight = graph->edge[j].weight;
            if (dist[u] != INT_MAX && dist[u] + weight < dist[v]){
		altera=1;
                dist[v] = dist[u] + weight;
		pi[v]=u;
	    }
        }
	}
    }
 

    for (i = 0; i < E; i++)
    {
        u = graph->edge[i].from;
        v = graph->edge[i].to;
        weight = graph->edge[i].weight;
        if (dist[u] != INT_MAX && dist[u] + weight < dist[v]){
		dist[u] = INT_MIN;
		QUEUEput(u);
	}
    }
    


    while(!QUEUEempty()){
	v=QUEUEget();	
	for(x= graph->adj[v];x != NULL ; x=x->next){
		w = x->v;
		if(dist[w]!=INT_MIN && pi[w]==v){
		dist[w] = INT_MIN;	
		QUEUEput(w);
		}
		
	}	
    }
	
    QUEUEfree(free);

    printDistancias(dist,pi, V);
}


/*Free Memory*/
void Graph_destroy(Graph* G, int maxV){
	int i;
	link current, next;
	for(i=0; i<=maxV-1; i++){
		current = G->adj[i];
		while(current != NULL){
			next=current->next;			
			free(current);
			current = next;			
		}	
		
	}
        free(G->edge);
	free(G->adj);
	free(G);
}


/*---Main Function---*/
int main()
{

    int maxV, maxE, from, i, cost, to, init;
    if(scanf("%d %d",&maxV, &maxE)<0) return 0;
    if(scanf("%d",&init)<0) return 0;
    Graph* graph = graph_Init(maxV, maxE);
    for(i=0; i<maxE;  i++){
	if(scanf("%d %d %d",&from, &to, &cost)<0) return 0;
	graph->edge[i].from = from-1;
    	graph->edge[i].to = to-1;
    	graph->edge[i].weight = cost;
	graph->adj[from-1] = New_Node(to-1, graph->adj[from-1]);
    }
    BellmanFordandNegativeCycle(graph, init-1);
    Graph_destroy(graph, maxV);
    return 0;
}

