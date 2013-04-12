#include <sys/time.h>
#include <sys/resource.h>
#include <unistd.h>

#include <cstdlib>
#include <limits>
#include <iostream>

using namespace std;

typedef unsigned long long xtime;

xtime getcputime()
{
  struct rusage ru;
  getrusage(RUSAGE_SELF, &ru);
  return (ru.ru_utime.tv_sec + ru.ru_stime.tv_sec ) * 1000000 +
          ru.ru_utime.tv_usec + ru.ru_stime.tv_usec;
}

void* hebra(void*)
{
  return NULL;
}

int main(int argc, char* argv[])
{  
  long diff, n = 10000, min = numeric_limits<long>::max(), sum = 0;
  struct timespec ts, ts2;
  
  pthread_t id;

  for (int i = 0; i < n; ++i)
    {
      clock_gettime(CLOCK_REALTIME, &ts);

      pthread_create(&id, NULL, hebra, NULL);
      pthread_join(id, NULL);
      
      clock_gettime(CLOCK_REALTIME, &ts2);

      diff = (ts2.tv_sec - ts.tv_sec) * 1000000000 + 
	(ts2.tv_nsec - ts.tv_nsec);
      
      if (diff < min)
        min = diff;
      sum += diff;
    }
  
  cout << "crear y ejecutar una hebra nula emplea: " << endl
       << "media : " << sum / (double)n << " ns" << endl
       << "minimo: " << min << " ns" << endl;
  
  return EXIT_SUCCESS;
}
