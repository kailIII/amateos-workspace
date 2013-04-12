/* Include files */

#include "conmutador_sfun.h"
#include "c2_conmutador.h"

/* Type Definitions */

/* Named Constants */

/* Variable Declarations */

/* Variable Definitions */
uint8_T _sfEvent_;
uint32_T _conmutadorMachineNumber_;
real_T _sfTime_;

/* Function Declarations */

/* Function Definitions */
void conmutador_initializer(void)
{
  _sfEvent_ = CALL_EVENT;
}

void conmutador_terminator(void)
{
}

/* SFunction Glue Code */
unsigned int sf_conmutador_method_dispatcher(SimStruct *simstructPtr, unsigned
  int chartFileNumber, int_T method, void *data)
{
  if (chartFileNumber==2) {
    c2_conmutador_method_dispatcher(simstructPtr, method, data);
    return 1;
  }

  return 0;
}

unsigned int sf_conmutador_process_check_sum_call( int nlhs, mxArray * plhs[],
  int nrhs, const mxArray * prhs[] )
{

#ifdef MATLAB_MEX_FILE

  char commandName[20];
  if (nrhs<1 || !mxIsChar(prhs[0]) )
    return 0;

  /* Possible call to get the checksum */
  mxGetString(prhs[0], commandName,sizeof(commandName)/sizeof(char));
  commandName[(sizeof(commandName)/sizeof(char)-1)] = '\0';
  if (strcmp(commandName,"sf_get_check_sum"))
    return 0;
  plhs[0] = mxCreateDoubleMatrix( 1,4,mxREAL);
  if (nrhs>1 && mxIsChar(prhs[1])) {
    mxGetString(prhs[1], commandName,sizeof(commandName)/sizeof(char));
    commandName[(sizeof(commandName)/sizeof(char)-1)] = '\0';
    if (!strcmp(commandName,"machine")) {
      ((real_T *)mxGetPr((plhs[0])))[0] = (real_T)(3943902053U);
      ((real_T *)mxGetPr((plhs[0])))[1] = (real_T)(656793924U);
      ((real_T *)mxGetPr((plhs[0])))[2] = (real_T)(2219970517U);
      ((real_T *)mxGetPr((plhs[0])))[3] = (real_T)(2251345687U);
    } else if (!strcmp(commandName,"exportedFcn")) {
      ((real_T *)mxGetPr((plhs[0])))[0] = (real_T)(0U);
      ((real_T *)mxGetPr((plhs[0])))[1] = (real_T)(0U);
      ((real_T *)mxGetPr((plhs[0])))[2] = (real_T)(0U);
      ((real_T *)mxGetPr((plhs[0])))[3] = (real_T)(0U);
    } else if (!strcmp(commandName,"makefile")) {
      ((real_T *)mxGetPr((plhs[0])))[0] = (real_T)(1902483543U);
      ((real_T *)mxGetPr((plhs[0])))[1] = (real_T)(2999140743U);
      ((real_T *)mxGetPr((plhs[0])))[2] = (real_T)(2508187389U);
      ((real_T *)mxGetPr((plhs[0])))[3] = (real_T)(885337318U);
    } else if (nrhs==3 && !strcmp(commandName,"chart")) {
      unsigned int chartFileNumber;
      chartFileNumber = (unsigned int)mxGetScalar(prhs[2]);
      switch (chartFileNumber) {
       case 2:
        {
          extern void sf_c2_conmutador_get_check_sum(mxArray *plhs[]);
          sf_c2_conmutador_get_check_sum(plhs);
          break;
        }

       default:
        ((real_T *)mxGetPr((plhs[0])))[0] = (real_T)(0.0);
        ((real_T *)mxGetPr((plhs[0])))[1] = (real_T)(0.0);
        ((real_T *)mxGetPr((plhs[0])))[2] = (real_T)(0.0);
        ((real_T *)mxGetPr((plhs[0])))[3] = (real_T)(0.0);
      }
    } else if (!strcmp(commandName,"target")) {
      ((real_T *)mxGetPr((plhs[0])))[0] = (real_T)(432618679U);
      ((real_T *)mxGetPr((plhs[0])))[1] = (real_T)(479728436U);
      ((real_T *)mxGetPr((plhs[0])))[2] = (real_T)(2825556576U);
      ((real_T *)mxGetPr((plhs[0])))[3] = (real_T)(870097265U);
    } else {
      return 0;
    }
  } else {
    ((real_T *)mxGetPr((plhs[0])))[0] = (real_T)(1268044517U);
    ((real_T *)mxGetPr((plhs[0])))[1] = (real_T)(1414080194U);
    ((real_T *)mxGetPr((plhs[0])))[2] = (real_T)(3914848975U);
    ((real_T *)mxGetPr((plhs[0])))[3] = (real_T)(2420896097U);
  }

  return 1;

#else

  return 0;

#endif

}

unsigned int sf_conmutador_autoinheritance_info( int nlhs, mxArray * plhs[], int
  nrhs, const mxArray * prhs[] )
{

#ifdef MATLAB_MEX_FILE

  char commandName[32];
  if (nrhs<2 || !mxIsChar(prhs[0]) )
    return 0;

  /* Possible call to get the autoinheritance_info */
  mxGetString(prhs[0], commandName,sizeof(commandName)/sizeof(char));
  commandName[(sizeof(commandName)/sizeof(char)-1)] = '\0';
  if (strcmp(commandName,"get_autoinheritance_info"))
    return 0;

  {
    unsigned int chartFileNumber;
    chartFileNumber = (unsigned int)mxGetScalar(prhs[1]);
    switch (chartFileNumber) {
     case 2:
      {
        extern mxArray *sf_c2_conmutador_get_autoinheritance_info(void);
        plhs[0] = sf_c2_conmutador_get_autoinheritance_info();
        break;
      }

     default:
      plhs[0] = mxCreateDoubleMatrix(0,0,mxREAL);
    }
  }

  return 1;

#else

  return 0;

#endif

}

unsigned int sf_conmutador_get_eml_resolved_functions_info( int nlhs, mxArray *
  plhs[], int nrhs, const mxArray * prhs[] )
{

#ifdef MATLAB_MEX_FILE

  char commandName[64];
  if (nrhs<2 || !mxIsChar(prhs[0]))
    return 0;

  /* Possible call to get the get_eml_resolved_functions_info */
  mxGetString(prhs[0], commandName,sizeof(commandName)/sizeof(char));
  commandName[(sizeof(commandName)/sizeof(char)-1)] = '\0';
  if (strcmp(commandName,"get_eml_resolved_functions_info"))
    return 0;

  {
    unsigned int chartFileNumber;
    chartFileNumber = (unsigned int)mxGetScalar(prhs[1]);
    switch (chartFileNumber) {
     case 2:
      {
        extern const mxArray *sf_c2_conmutador_get_eml_resolved_functions_info
          (void);
        mxArray *persistentMxArray = (mxArray *)
          sf_c2_conmutador_get_eml_resolved_functions_info();
        plhs[0] = mxDuplicateArray(persistentMxArray);
        mxDestroyArray(persistentMxArray);
        break;
      }

     default:
      plhs[0] = mxCreateDoubleMatrix(0,0,mxREAL);
    }
  }

  return 1;

#else

  return 0;

#endif

}

void conmutador_debug_initialize(void)
{
  _conmutadorMachineNumber_ = sf_debug_initialize_machine("conmutador","sfun",0,
    1,0,0,0);
  sf_debug_set_machine_event_thresholds(_conmutadorMachineNumber_,0,0);
  sf_debug_set_machine_data_thresholds(_conmutadorMachineNumber_,0);
}

void conmutador_register_exported_symbols(SimStruct* S)
{
}
