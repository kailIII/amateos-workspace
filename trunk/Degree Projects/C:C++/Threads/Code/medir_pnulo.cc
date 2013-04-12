#include <sys/time.h>
#include <sys/resource.h>
#include <unistd.h>

#include <cstdlib>
#include <iostream>
#include <limits>

using namespace std;

int main(int argc, char* argv[])
{
  long diff, n = 100, min = numeric_limits<long>::max(), sum = 0;
  struct timespec ts, ts2;
  
  for (int i = 0; i < n; ++i)
    {
      clock_gettime(CLOCK_REALTIME, &ts);

      switch(fork())
	{
	case -1: cerr << argv[0] << ": error en fork" << endl; exit(-1);
	case 0:  execl("pnulo", "pnulo", NULL); break;
	default: wait(); break;
	}

      clock_gettime(CLOCK_REALTIME, &ts2);

      diff = (ts2.tv_sec - ts.tv_sec) * 1000000000 + 
	(ts2.tv_nsec - ts.tv_nsec);
      
      if (diff < min)
        min = diff;
      sum += diff;
    }
  
  cout << "crear y ejecutar un proceso nulo emplea: " << endl
       << "media : " << sum / (double)n << " ns" << endl
       << "minimo: " << min << " ns" << endl;
  
  return EXIT_SUCCESS;
}
