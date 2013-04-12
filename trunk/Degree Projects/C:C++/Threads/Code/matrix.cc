//-----------------------------------------------------------------------------
// matrix.cc
//-----------------------------------------------------------------------------

#include <iostream>
#include <sstream>
#include <matrix.h>

using namespace std;

//-----------------------------------------------------------------------------

int main(int argc, char *argv[])
{
  const int param = 3;

  if (argc != param + 1)
    {
      cerr << "uso: " << argv[0] << " #filas #columnas #hebras" << endl;
      return EXIT_FAILURE;
    }
  
  unsigned v[param];

  for (int i = 0; i < param; ++i)
    {
      istringstream iss(argv[i + 1]);
      
      iss >> v[i];
	
	if (!iss)
	  {
	    cerr << argv[0] << ": " << argv[i + 1] 
		 << " no es un numero válido" << endl;
	    return EXIT_FAILURE;
	  }
    }
 
  NUM_THREADS = v[param - 1]; // número de hebras... definido en matrix.h
 
  srandom(v[0]);    // semilla aleatoria

  matrix a(v[0], v[1]);
  a.random();
 
  matrix t(a);
  t.transpose();
  
  matrix s(a);
  s *= s;

  matrix o(v[0], v[1], 1);

  a = a * t + s - o;

  return a[0][0];
}

//-----------------------------------------------------------------------------

// Local Variables: 
// mode:C++ 
// End:
