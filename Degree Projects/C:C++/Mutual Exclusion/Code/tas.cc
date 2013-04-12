//-----------------------------------------------------------------------------
// tas.cc
//-----------------------------------------------------------------------------

#include <pthread.h>
#include <unistd.h>
#include <iostream>

//-----------------------------------------------------------------------------

using namespace std;

//-----------------------------------------------------------------------------

bool testandset (volatile bool *spinlock)
{
  bool ret;
  __asm__ __volatile__("lock xchgb %0, %1"
                       : "=r"(ret), "=m"(*spinlock)
                       : "0"(true), "m"(*spinlock)
                       : "memory");
  return ret;
}

//-----------------------------------------------------------------------------

class cerrojo_t
{
public:
	cerrojo_t(): cerrado(false){}
  void adquirir()
  {
 
	while(cerrado);
	cerrado=true;
  }
  
  void liberar()
  {
	cerrado=false;
  }
  
private: volatile bool cerrado;  
} cerrojo;
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
      cerrojo.adquirir();
      seccion_critica();
      cerrojo.liberar();
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
