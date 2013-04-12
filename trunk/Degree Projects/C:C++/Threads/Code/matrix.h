//-----------------------------------------------------------------------------
// matrix.h
//-----------------------------------------------------------------------------

#ifndef MATRIX_H
#define MATRIX_H

//-----------------------------------------------------------------------------

#include <cassert>
#include <cstdlib>
#include <pthread.h>
#include <iostream>
#include <vector>

//-----------------------------------------------------------------------------

unsigned NUM_THREADS = 2;

//-----------------------------------------------------------------------------

class matrix;

struct parametros {
    unsigned numero_hebra;
    const matrix *a;
    const matrix *b;
    matrix *r;
  };

  
  
void* multiplicar(void*);
 //--------------------------------------------------------------------------

class matrix: public std::vector<std::vector<double> >
{
public:
  matrix(unsigned __r, unsigned __c, double __value = 0): 
    std::vector<std::vector<double> >(__r, std::vector<double>(__c, __value)) 
  {}
  
  void random()
  {
    for (matrix::iterator i = this->begin(); 
	 i != this->end(); 
	 ++i)
      for (std::vector<double>::iterator j = i->begin(); 
 	   j != i->end(); 
 	   ++j)
	*j = ::random() % 9 + 1;
  }
    
  void transpose()
  {
    unsigned r = this->size(), c = this->front().size();
    matrix m(c, r);
    
    for (unsigned i = 0; i < r; ++i)
      for (unsigned j = 0; j < c; ++j)
	m[j][i] = (*this)[i][j];

    (*this) = m;
  }
  
  matrix& operator+=(const matrix& m)
  {
    unsigned r = this->size(), c = this->front().size();
    
    assert(m.size() == r && m.front().size() == c);
    
    for (unsigned i = 0; i < r; ++i)
      for (unsigned j = 0; j < c; ++j)
	(*this)[i][j] += m[i][j];

    return *this;
  }
  
  matrix& operator-=(const matrix& m)
  {
    unsigned r = this->size(), c = this->front().size();
    
    assert(m.size() == r && m.front().size() == c);
    
    for (unsigned i = 0; i < r; ++i)
      for (unsigned j = 0; j < c; ++j)
	(*this)[i][j] -= m[i][j];

    return *this;
  }



void *hola(void *param)
{
 return NULL; 
}
 matrix& operator*=(const matrix& m)
  {
    
    pthread_t thread_id[NUM_THREADS];	//identificadores de hebra
    int rc;				//codigo de retorno al crear la hebra
    
    unsigned r1 = this->size(), c1 = this->front().size();
    unsigned r2 =     m.size(), c2 =     m.front().size();

    parametros p;			// argumentos para la funcion multiplicar
    //p.a=this;				
    p.b=&m;
    matrix resultado(r1,c2);		//matriz resultado de la multiplicacion
    //p.r=&resultado;
    
    assert(c1 == r2);

    //i<max(NUM_THREADS, r1)
  
    for (unsigned i = 0; i < NUM_THREADS; i ++)
      {
	//p.numero_hebra=i;
	rc=pthread_create(&thread_id[i],NULL, multiplicar, &p);
	if (rc)
	    {
	      printf("ERROR; código de retorno de pthread_create() es %d\n", rc);
	      exit(-1);
	    }
    }
   
    //i<max(NUM_THREADS,r1)
    for (unsigned i=0; i<NUM_THREADS; i++)
      {
	rc=pthread_join(thread_id[i],NULL);
	if (rc)
	    {
	      printf("ERROR; código de retorno de pthread_join() es %d\n", rc);
	      exit(-1);
	    }
    }
    matrix& res=*(p.r);			//hacemos esta asignacion porque hay que devolver una referencia
    return res;
  }//fin operator *=
  

  friend std::ostream& operator<<(std::ostream& os, const matrix& m)
  {
    for (matrix::const_iterator i = m.begin(); 
	 i != m.end(); 
	 ++i)
      {
	for (std::vector<double>::const_iterator j = i->begin(); 
	     j != i->end(); 
	     ++j)
	  std::cout << *j << " ";
	std::cout << std::endl;
      }
    return os;
  }
};

matrix operator+(const matrix& __x, const matrix& __y)
{
  matrix __r = __x;
  __r += __y;
  return __r;
}

matrix operator-(const matrix& __x, const matrix& __y)
{
  matrix __r = __x;
  __r -= __y;
  return __r;
}

matrix operator*(const matrix& __x, const matrix& __y)
{
  matrix __r = __x;
  __r *= __y;
  return __r;
}


//-------------------------------------------------------------------------------

void* multiplicar(void* param)
  {
    parametros *p=(parametros *) param;
    
    //numero de hebra que se ejecuta
    int numero_hebra=(*p).numero_hebra;
    //const matrix *a=(*p).a;
    //unsigned i = (*a).size();
    unsigned r1 = (*((*p).a)).size(), c1 =(*((*p).a)).front().size();
    unsigned r2 = (*((*p).b)).size(), c2 =(*((*p).b)).front().size();
    
    for (unsigned i =numero_hebra; i < r1; i=i+NUM_THREADS)
      for (unsigned j = 0; j < c2; ++j)
	for (unsigned k = 0; k < c1; ++k)
	  (*((*p).r))[i][j] += (*((*p).a))[i][k] * (*((*p).b))[k][i];
    
     return NULL;
  }//fin multiplicar

//-----------------------------------------------------------------------------

#endif // MATRIX_H

//-----------------------------------------------------------------------------

// Local Variables: 
// mode:C++ 
// End:
