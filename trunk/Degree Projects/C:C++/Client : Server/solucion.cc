//-----------------------------------------------------------------------------
// cliente-servidor.cc
//-----------------------------------------------------------------------------

#include <pthread.h> // pthread_create pthread_join pthread_t
#include <unistd.h>  // alarm sleep
#include <csignal>   // signal SIGALRM
#include <iostream>  // endl cout 

//-----------------------------------------------------------------------------

using namespace std;

//-----------------------------------------------------------------------------

pthread_t id_cliente, id_servidor; // identificadores de hebras

//-----------------------------------------------------------------------------

void manejador_sigusr2(int)
{
  signal(SIGUSR2, manejador_sigusr2);
  cout << "[cliente ]: respueta recibida"  << endl;
}

void *hebra_cliente(void*)
{
  signal(SIGUSR2, manejador_sigusr2);

  while(true)
    {
      pthread_kill(id_servidor, SIGUSR1);
      cout << "[cliente ]: petición enviada"  << endl; 
    }
  
  return NULL;
}

//-----------------------------------------------------------------------------

void manejador_sigusr1(int)
{
  signal(SIGUSR1, manejador_sigusr1);
  cout << "[servidor]: petición recibida"  << endl;
  //  sleep(1); // dormir durante 1s segundo para simular carga de trabajo
  pthread_kill(id_servidor, SIGUSR2);
  cout << "[servidor]: respuesta enviada"  << endl;
}

void *hebra_servidor(void*)
{  
  signal(SIGUSR1, manejador_sigusr1);

  while (true)
    pthread_yield();

  return NULL;
}

//-----------------------------------------------------------------------------

int main()
{
  pthread_create(&id_servidor, NULL, hebra_servidor, NULL); // el orden importa
  pthread_create(&id_cliente , NULL, hebra_cliente , NULL); // el orden importa
  
  pthread_join(id_servidor, NULL);
  pthread_join(id_cliente , NULL);
}

//-----------------------------------------------------------------------------

// Local Variables: 
// mode:C++ 
// End:
