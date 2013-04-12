//-----------------------------------------------------------------------------
// panaderia.cc
//-----------------------------------------------------------------------------

#include <pthread.h>
#include <unistd.h>
#include <iostream>

//-----------------------------------------------------------------------------

using namespace std;

//-----------------------------------------------------------------------------

void seccion_critica()
{
  cout << "[" << pthread_self() << "]: ";
  for(unsigned i = 0; i < 10; ++i)
    cout << i;
  cout << endl;    
}

//-----------------------------------------------------------------------------

void* hebra(void*)
{
  while(true)
    {
      seccion_critica();
    }
  return NULL;
}

//-----------------------------------------------------------------------------

int main()
{
  const unsigned N = 100;
  pthread_t id[N];
  
  alarm(2);

  for(unsigned i = 0; i < N; ++i)
    pthread_create(&id[i], NULL, hebra, NULL);
  
  for(unsigned i = 0; i < N; ++i)
    pthread_join(id[i], NULL);
}

//-----------------------------------------------------------------------------
