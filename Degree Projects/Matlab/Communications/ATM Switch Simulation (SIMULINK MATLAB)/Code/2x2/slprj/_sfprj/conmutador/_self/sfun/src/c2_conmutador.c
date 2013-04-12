/* Include files */

#include "conmutador_sfun.h"
#include "c2_conmutador.h"
#define CHARTINSTANCE_CHARTNUMBER      (chartInstance.chartNumber)
#define CHARTINSTANCE_INSTANCENUMBER   (chartInstance.instanceNumber)
#include "conmutador_sfun_debug_macros.h"

/* Type Definitions */

/* Named Constants */
#define c2_IN_NO_ACTIVE_CHILD          (0)

/* Variable Declarations */

/* Variable Definitions */
static SFc2_conmutadorInstanceStruct chartInstance;

/* Function Declarations */
static void initialize_c2_conmutador(void);
static void initialize_params_c2_conmutador(void);
static void enable_c2_conmutador(void);
static void disable_c2_conmutador(void);
static void finalize_c2_conmutador(void);
static void sf_c2_conmutador(void);
static void c2_c2_conmutador(void);
static void init_script_number_translation(uint32_T c2_machineNumber, uint32_T
  c2_chartNumber);
static real_T c2_rand(void);
static const mxArray *c2_sf_marshall(void *c2_chartInstance, void *c2_u);
static const mxArray *c2_b_sf_marshall(void *c2_chartInstance, void *c2_u);
static const mxArray *c2_c_sf_marshall(void *c2_chartInstance, void *c2_u);
static const mxArray *c2_d_sf_marshall(void *c2_chartInstance, void *c2_u);
static void c2_info_helper(c2_ResolvedFunctionInfo c2_info[56]);
static void init_dsm_address_info(void);

/* Function Definitions */
static void initialize_c2_conmutador(void)
{
  _sfTime_ = (real_T)ssGetT(chartInstance.S);
  chartInstance.c2_twister_state_not_empty = false;
  chartInstance.c2_method_not_empty = false;
  chartInstance.c2_v4_state_not_empty = false;
  chartInstance.c2_is_active_c2_conmutador = 0U;
}

static void initialize_params_c2_conmutador(void)
{
}

static void enable_c2_conmutador(void)
{
  _sfTime_ = (real_T)ssGetT(chartInstance.S);
}

static void disable_c2_conmutador(void)
{
  _sfTime_ = (real_T)ssGetT(chartInstance.S);
}

static void finalize_c2_conmutador(void)
{
}

static void sf_c2_conmutador(void)
{
  uint8_T c2_previousEvent;
  real_T *c2_destino1;
  real_T *c2_tx_drop1;
  real_T *c2_destino2;
  real_T *c2_tx_drop2;
  real_T *c2_tx_drop3;
  real_T *c2_tx_drop4;
  c2_tx_drop1 = (real_T *)ssGetOutputPortSignal(chartInstance.S, 1);
  c2_tx_drop4 = (real_T *)ssGetOutputPortSignal(chartInstance.S, 4);
  c2_destino2 = (real_T *)ssGetInputPortSignal(chartInstance.S, 1);
  c2_tx_drop2 = (real_T *)ssGetOutputPortSignal(chartInstance.S, 2);
  c2_destino1 = (real_T *)ssGetInputPortSignal(chartInstance.S, 0);
  c2_tx_drop3 = (real_T *)ssGetOutputPortSignal(chartInstance.S, 3);
  _sfTime_ = (real_T)ssGetT(chartInstance.S);
  _SFD_CC_CALL(CHART_ENTER_SFUNCTION_TAG,0);
  _SFD_DATA_RANGE_CHECK(*c2_destino1, 0U);
  _SFD_DATA_RANGE_CHECK(*c2_tx_drop1, 1U);
  _SFD_DATA_RANGE_CHECK(*c2_destino2, 2U);
  _SFD_DATA_RANGE_CHECK(*c2_tx_drop2, 3U);
  _SFD_DATA_RANGE_CHECK(*c2_tx_drop3, 4U);
  _SFD_DATA_RANGE_CHECK(*c2_tx_drop4, 5U);
  c2_previousEvent = _sfEvent_;
  _sfEvent_ = CALL_EVENT;
  c2_c2_conmutador();
  _sfEvent_ = c2_previousEvent;
  sf_debug_check_for_state_inconsistency(_conmutadorMachineNumber_,
    chartInstance.chartNumber, chartInstance.instanceNumber);
}

static void c2_c2_conmutador(void)
{
  real_T c2_destino1;
  real_T c2_destino2;
  real_T c2_nargout = 4.0;
  real_T c2_nargin = 2.0;
  real_T c2_entradas_en_conflicto[2];
  real_T c2_j;
  real_T c2_i;
  real_T c2_destinos[2];
  real_T c2_matriz_com[4];
  real_T c2_tx_drop4;
  real_T c2_tx_drop3;
  real_T c2_tx_drop2;
  real_T c2_tx_drop1;
  int32_T c2_i0;
  int32_T c2_i1;
  real_T c2_d0;
  real_T c2_b_i;
  real_T c2_destino;
  real_T c2_b_nargout = 1.0;
  real_T c2_b_nargin = 1.0;
  real_T c2_puertos[2];
  int32_T c2_i2;
  int32_T c2_c_i;
  int32_T c2_i3;
  static int32_T c2_iv0[2] = { 0, 1 };

  real_T c2_d1;
  real_T c2_b_j;
  int32_T c2_i4;
  real_T c2_d2;
  real_T c2_d_i;
  int32_T c2_i5;
  real_T c2_x[2];
  int32_T c2_i6;
  real_T c2_b_x[2];
  real_T c2_c_x;
  real_T c2_y;
  real_T c2_d_x;
  real_T c2_r;
  int32_T c2_i7;
  real_T c2_entradas[2];
  real_T c2_c_nargout = 1.0;
  real_T c2_c_nargin = 1.0;
  real_T c2_e_i;
  real_T c2_seleccion;
  real_T c2_aux;
  real_T c2_ent;
  real_T c2_cont;
  real_T c2_columna[2];
  int32_T c2_i8;
  int32_T c2_i9;
  real_T c2_e_x[2];
  int32_T c2_i10;
  real_T c2_f_x[2];
  real_T c2_g_x;
  real_T c2_h_x;
  real_T c2_b_r;
  real_T c2_a;
  real_T c2_b;
  real_T c2_i_x;
  real_T c2_d3;
  real_T c2_f_i;
  int32_T c2_c_j;
  int32_T c2_i11;
  real_T *c2_b_destino1;
  real_T *c2_b_destino2;
  real_T *c2_b_tx_drop1;
  real_T *c2_b_tx_drop2;
  real_T *c2_b_tx_drop3;
  real_T *c2_b_tx_drop4;
  c2_b_tx_drop1 = (real_T *)ssGetOutputPortSignal(chartInstance.S, 1);
  c2_b_tx_drop4 = (real_T *)ssGetOutputPortSignal(chartInstance.S, 4);
  c2_b_destino2 = (real_T *)ssGetInputPortSignal(chartInstance.S, 1);
  c2_b_tx_drop2 = (real_T *)ssGetOutputPortSignal(chartInstance.S, 2);
  c2_b_destino1 = (real_T *)ssGetInputPortSignal(chartInstance.S, 0);
  c2_b_tx_drop3 = (real_T *)ssGetOutputPortSignal(chartInstance.S, 3);
  _SFD_CC_CALL(CHART_ENTER_DURING_FUNCTION_TAG,0);
  c2_destino1 = *c2_b_destino1;
  c2_destino2 = *c2_b_destino2;
  sf_debug_symbol_scope_push(13U, 0U);
  sf_debug_symbol_scope_add_verbose("nargout", 0, 0U, 0U, 0U, 0U, 1.0, 0, 0.0,
    0U, 0, 0U, 0, 0, &c2_nargout);
  sf_debug_symbol_scope_add_verbose("nargin", 0, 0U, 0U, 0U, 0U, 1.0, 0, 0.0, 0U,
    0, 0U, 0, 0, &c2_nargin);
  sf_debug_symbol_scope_add("entradas_en_conflicto", &c2_entradas_en_conflicto,
    c2_c_sf_marshall);
  sf_debug_symbol_scope_add("j", &c2_j, c2_sf_marshall);
  sf_debug_symbol_scope_add("i", &c2_i, c2_sf_marshall);
  sf_debug_symbol_scope_add("destinos", &c2_destinos, c2_c_sf_marshall);
  sf_debug_symbol_scope_add("matriz_com", &c2_matriz_com, c2_b_sf_marshall);
  sf_debug_symbol_scope_add("tx_drop4", &c2_tx_drop4, c2_sf_marshall);
  sf_debug_symbol_scope_add("tx_drop3", &c2_tx_drop3, c2_sf_marshall);
  sf_debug_symbol_scope_add("tx_drop2", &c2_tx_drop2, c2_sf_marshall);
  sf_debug_symbol_scope_add("tx_drop1", &c2_tx_drop1, c2_sf_marshall);
  sf_debug_symbol_scope_add("destino2", &c2_destino2, c2_sf_marshall);
  sf_debug_symbol_scope_add("destino1", &c2_destino1, c2_sf_marshall);
  CV_EML_FCN(0, 0);

  /* matriz de conmutacion: filas=entradas, columnas=salidas */
  _SFD_EML_CALL(0,4);
  for (c2_i0 = 0; c2_i0 < 2; c2_i0 = c2_i0 + 1) {
    for (c2_i1 = 0; c2_i1 < 2; c2_i1 = c2_i1 + 1) {
      c2_matriz_com[c2_i1 + 2 * c2_i0] = 1.0;
    }
  }

  /* vector de destinos de cada entrada */
  _SFD_EML_CALL(0,7);
  c2_destinos[0] = c2_destino1;
  c2_destinos[1] = c2_destino2;

  /* Rellenamos la matriz de conmutación con los destinos correspondientes */
  c2_d0 = 1.0;
  c2_i = c2_d0;
  c2_b_i = c2_d0;
  while (c2_b_i <= 2.0) {
    c2_i = c2_b_i;
    CV_EML_FOR(0, 0, 1);
    _SFD_EML_CALL(0,11);
    c2_destino = c2_destinos[(int32_T)_SFD_EML_ARRAY_BOUNDS_CHECK("destinos",
      (int32_T)_SFD_INTEGER_CHECK("i", c2_i), 1, 2, 1, 0) - 1];
    sf_debug_symbol_scope_push(4U, 0U);
    sf_debug_symbol_scope_add_verbose("nargout", 0, 0U, 0U, 0U, 0U, 1.0, 0, 0.0,
      0U, 0, 0U, 0, 0, &c2_b_nargout);
    sf_debug_symbol_scope_add_verbose("nargin", 0, 0U, 0U, 0U, 0U, 1.0, 0, 0.0,
      0U, 0, 0U, 0, 0, &c2_b_nargin);
    sf_debug_symbol_scope_add("puertos", &c2_puertos, c2_c_sf_marshall);
    sf_debug_symbol_scope_add("destino", &c2_destino, c2_sf_marshall);
    CV_EML_FCN(0, 1);

    /* %%%%%%% Funciones adicionales */
    /* Introduce un 2 (la entrada quiere salir por la fila relativa a esa columna) en la columna */
    /* correspondiente de la matriz para una entrada. */
    _SFD_EML_CALL(0,42);
    for (c2_i2 = 0; c2_i2 < 2; c2_i2 = c2_i2 + 1) {
      c2_puertos[c2_i2] = 1.0;
    }

    _SFD_EML_CALL(0,43);
    if (CV_EML_IF(0, 2, c2_destino != 0.0)) {
      /* Si es cero, el paquete debe descartarse -> se deja en la matriz un 1 */
      _SFD_EML_CALL(0,44);
      c2_puertos[(int32_T)_SFD_EML_ARRAY_BOUNDS_CHECK("puertos", (int32_T)
        _SFD_INTEGER_CHECK("destino", c2_destino), 1, 2, 1, 0) - 1] =
        2.0;
    }

    _SFD_EML_CALL(0,-44);
    sf_debug_symbol_scope_pop();
    c2_c_i = (int32_T)_SFD_EML_ARRAY_BOUNDS_CHECK("matriz_com", (int32_T)
      _SFD_INTEGER_CHECK("i", c2_i), 1, 2, 1, 0) - 1;
    for (c2_i3 = 0; c2_i3 < 2; c2_i3 = c2_i3 + 1) {
      c2_matriz_com[c2_c_i + 2 * c2_iv0[c2_i3]] = c2_puertos[c2_i3];
    }

    c2_b_i = c2_b_i + 1.0;
    sf_mex_listen_for_ctrl_c(chartInstance.S);
  }

  CV_EML_FOR(0, 0, 0);

  /* Comprobamos las columnas de la matriz. Si en una columna hay más de un elemento que sea un 2-> conflicto */
  c2_d1 = 1.0;
  c2_j = c2_d1;
  c2_b_j = c2_d1;
  while (c2_b_j <= 2.0) {
    c2_j = c2_b_j;
    CV_EML_FOR(0, 1, 1);
    _SFD_EML_CALL(0,16);
    for (c2_i4 = 0; c2_i4 < 2; c2_i4 = c2_i4 + 1) {
      c2_entradas_en_conflicto[c2_i4] = 0.0;
    }

    /* Vector que almacena aquellas entradas que generan el conflicto */
    /* verificamos las entradas que quieren salir por la salida j */
    c2_d2 = 1.0;
    c2_i = c2_d2;
    c2_d_i = c2_d2;
    while (c2_d_i <= 2.0) {
      c2_i = c2_d_i;
      CV_EML_FOR(0, 2, 1);
      _SFD_EML_CALL(0,19);
      if (CV_EML_IF(0, 0, c2_matriz_com[((int32_T)_SFD_EML_ARRAY_BOUNDS_CHECK(
             "matriz_com", (int32_T)_SFD_INTEGER_CHECK("i", c2_i), 1, 2, 1
             , 0) - 1) + 2 * ((int32_T)_SFD_EML_ARRAY_BOUNDS_CHECK("matriz_com",
             (int32_T)_SFD_INTEGER_CHECK("j", c2_j), 1, 2, 2, 0) - 1)] == 2.0
                    )) {
        _SFD_EML_CALL(0,20);
        c2_entradas_en_conflicto[(int32_T)_SFD_EML_ARRAY_BOUNDS_CHECK(
          "entradas_en_conflicto", (int32_T)_SFD_INTEGER_CHECK("i", c2_i), 1, 2
          , 1, 0) - 1] = 1.0;
      }

      c2_d_i = c2_d_i + 1.0;
      sf_mex_listen_for_ctrl_c(chartInstance.S);
    }

    CV_EML_FOR(0, 2, 0);

    /* Comprobamos si hay conflicto */
    _SFD_EML_CALL(0,25);
    for (c2_i5 = 0; c2_i5 < 2; c2_i5 = c2_i5 + 1) {
      c2_x[c2_i5] = c2_entradas_en_conflicto[c2_i5];
    }

    for (c2_i6 = 0; c2_i6 < 2; c2_i6 = c2_i6 + 1) {
      c2_b_x[c2_i6] = c2_x[c2_i6];
    }

    c2_c_x = c2_b_x[0];
    c2_y = c2_c_x;
    c2_d_x = c2_b_x[1];
    c2_r = c2_d_x;
    c2_y = c2_y + c2_r;
    if (CV_EML_IF(0, 1, c2_y > 1.0)) {
      /* Resolvemos el conflicto */
      _SFD_EML_CALL(0,27);
      for (c2_i7 = 0; c2_i7 < 2; c2_i7 = c2_i7 + 1) {
        c2_entradas[c2_i7] = c2_entradas_en_conflicto[c2_i7];
      }

      sf_debug_symbol_scope_push(9U, 0U);
      sf_debug_symbol_scope_add_verbose("nargout", 0, 0U, 0U, 0U, 0U, 1.0, 0,
        0.0, 0U, 0, 0U, 0, 0, &c2_c_nargout);
      sf_debug_symbol_scope_add_verbose("nargin", 0, 0U, 0U, 0U, 0U, 1.0, 0, 0.0,
        0U, 0, 0U, 0, 0, &c2_c_nargin);
      sf_debug_symbol_scope_add("i", &c2_e_i, c2_sf_marshall);
      sf_debug_symbol_scope_add("seleccion", &c2_seleccion, c2_sf_marshall);
      sf_debug_symbol_scope_add("aux", &c2_aux, c2_sf_marshall);
      sf_debug_symbol_scope_add("ent", &c2_ent, c2_sf_marshall);
      sf_debug_symbol_scope_add("cont", &c2_cont, c2_sf_marshall);
      sf_debug_symbol_scope_add("columna", &c2_columna, c2_d_sf_marshall);
      sf_debug_symbol_scope_add("entradas", &c2_entradas, c2_c_sf_marshall);
      CV_EML_FCN(0, 2);

      /* Elige de forma aleatoria una entrada para resolver un conflicto y devuelve la columna que debería */
      /* tener la matriz de conmutación tras la resolución del mismo. */
      _SFD_EML_CALL(0,50);
      c2_cont = 0.0;
      _SFD_EML_CALL(0,51);
      c2_ent = 0.0;
      _SFD_EML_CALL(0,52);
      for (c2_i8 = 0; c2_i8 < 2; c2_i8 = c2_i8 + 1) {
        c2_columna[c2_i8] = 1.0;
      }

      _SFD_EML_CALL(0,53);
      for (c2_i9 = 0; c2_i9 < 2; c2_i9 = c2_i9 + 1) {
        c2_e_x[c2_i9] = c2_entradas[c2_i9];
      }

      for (c2_i10 = 0; c2_i10 < 2; c2_i10 = c2_i10 + 1) {
        c2_f_x[c2_i10] = c2_e_x[c2_i10];
      }

      c2_g_x = c2_f_x[0];
      c2_aux = c2_g_x;
      c2_h_x = c2_f_x[1];
      c2_b_r = c2_h_x;
      c2_aux = c2_aux + c2_b_r;

      /* Sumamos el vector de entradas para saber cuantas líneas hay en conflicto */
      _SFD_EML_CALL(0,54);
      c2_a = c2_aux;
      c2_b = c2_rand();
      c2_i_x = c2_a * c2_b;
      c2_seleccion = c2_i_x;
      c2_seleccion = ceil(c2_seleccion);

      /* Elegimos de forma aleatoria una de las entradas */
      c2_d3 = 1.0;
      c2_e_i = c2_d3;
      c2_f_i = c2_d3;
      while (c2_f_i <= 2.0) {
        c2_e_i = c2_f_i;
        CV_EML_FOR(0, 3, 1);
        _SFD_EML_CALL(0,57);
        if (CV_EML_IF(0, 3, c2_entradas[(int32_T)_SFD_EML_ARRAY_BOUNDS_CHECK(
              "entradas", (int32_T)_SFD_INTEGER_CHECK("i", c2_e_i), 1, 2, 1, 0
              ) - 1] == 1.0)) {
          _SFD_EML_CALL(0,58);
          c2_cont = c2_cont + 1.0;
          _SFD_EML_CALL(0,59);
          if (CV_EML_IF(0, 4, c2_cont == c2_seleccion)) {
            _SFD_EML_CALL(0,60);
            c2_ent = c2_e_i;
          }
        }

        c2_f_i = c2_f_i + 1.0;
        sf_mex_listen_for_ctrl_c(chartInstance.S);
      }

      CV_EML_FOR(0, 3, 0);
      _SFD_EML_CALL(0,64);
      c2_columna[(int32_T)_SFD_EML_ARRAY_BOUNDS_CHECK("columna", (int32_T)
        _SFD_INTEGER_CHECK("ent", c2_ent), 1, 2, 1, 0) - 1] = 2.0;

      /* Ponemos un 2 en la línea que finalmente sale ganadora del conflicto y dejamos las demás a 1 */
      _SFD_EML_CALL(0,-64);
      sf_debug_symbol_scope_pop();
      c2_c_j = (int32_T)_SFD_EML_ARRAY_BOUNDS_CHECK("matriz_com", (int32_T)
        _SFD_INTEGER_CHECK("j", c2_j), 1, 2, 2, 0) - 1;
      for (c2_i11 = 0; c2_i11 < 2; c2_i11 = c2_i11 + 1) {
        c2_matriz_com[c2_iv0[c2_i11] + 2 * c2_c_j] = c2_columna[c2_i11];
      }
    }

    c2_b_j = c2_b_j + 1.0;
    sf_mex_listen_for_ctrl_c(chartInstance.S);
  }

  CV_EML_FOR(0, 1, 0);

  /* Asignamos las señales tx_drop a sus valores correspondientes según la matriz de conmutación */
  _SFD_EML_CALL(0,32);
  c2_tx_drop1 = c2_matriz_com[0];
  _SFD_EML_CALL(0,33);
  c2_tx_drop2 = c2_matriz_com[2];
  _SFD_EML_CALL(0,34);
  c2_tx_drop3 = c2_matriz_com[1];
  _SFD_EML_CALL(0,35);
  c2_tx_drop4 = c2_matriz_com[3];
  _SFD_EML_CALL(0,-35);
  sf_debug_symbol_scope_pop();
  *c2_b_tx_drop1 = c2_tx_drop1;
  *c2_b_tx_drop2 = c2_tx_drop2;
  *c2_b_tx_drop3 = c2_tx_drop3;
  *c2_b_tx_drop4 = c2_tx_drop4;
  _SFD_CC_CALL(EXIT_OUT_OF_FUNCTION_TAG,0);
}

static void init_script_number_translation(uint32_T c2_machineNumber, uint32_T
  c2_chartNumber)
{
}

static real_T c2_rand(void)
{
  uint8_T c2_TWISTER;
  int32_T c2_i12;
  uint32_T c2_mt[625];
  real_T c2_seed;
  int32_T c2_i13;
  real_T c2_Nminus1;
  uint32_T c2_INIT_FACTOR;
  real_T c2_d4;
  uint32_T c2_u0;
  uint32_T c2_b_r;
  real_T c2_d5;
  real_T c2_mti;
  real_T c2_b_mti;
  real_T c2_d6;
  real_T c2_d7;
  uint32_T c2_u1;
  real_T c2_d8;
  real_T c2_d9;
  uint32_T c2_u2;
  int32_T c2_i14;
  uint32_T c2_state[625];
  int32_T c2_i15;
  uint32_T c2_b_twister_state[625];
  int32_T c2_i16;
  uint32_T c2_b_mt[625];
  int32_T c2_i17;
  uint32_T c2_b_state[625];
  real_T c2_N;
  real_T c2_M;
  real_T c2_NminusM;
  uint32_T c2_ONE;
  uint32_T c2_UPPER_MASK;
  uint32_T c2_LOWER_MASK;
  int32_T c2_i18;
  uint32_T c2_u[2];
  real_T c2_d10;
  real_T c2_j;
  real_T c2_b_j;
  uint32_T c2_c_mti;
  real_T c2_d11;
  real_T c2_kk;
  real_T c2_b_kk;
  uint32_T c2_y;
  uint32_T c2_b_y;
  uint32_T c2_c_y;
  uint32_T c2_b_ONE;
  real_T c2_d12;
  real_T c2_c_kk;
  uint32_T c2_d_y;
  uint32_T c2_e_y;
  uint32_T c2_c_ONE;
  uint32_T c2_f_y;
  uint32_T c2_g_y;
  uint32_T c2_d_ONE;
  int32_T c2_i19;
  int32_T c2_i20;
  uint32_T c2_b_u[2];
  real_T c2_a;
  real_T c2_b;
  real_T c2_h_y;
  real_T c2_b_b;
  real_T c2_b_a;
  real_T c2_d13;
  int32_T c2_i21;
  uint32_T c2_s;
  uint32_T c2_IA;
  uint32_T c2_IM;
  uint32_T c2_IQ;
  uint32_T c2_IR;
  uint32_T c2_u3;
  uint32_T c2_hi;
  uint32_T c2_lo;
  uint32_T c2_test1;
  uint32_T c2_test2;
  uint32_T c2_b_v4_state;
  real_T c2_c_a;
  real_T c2_c_b;
  real_T c2_d14;
  c2_TWISTER = 2U;
  if (!chartInstance.c2_method_not_empty) {
    chartInstance.c2_method = c2_TWISTER;
    chartInstance.c2_method_not_empty = true;
  }

  if (chartInstance.c2_method == c2_TWISTER) {
    if (!chartInstance.c2_twister_state_not_empty) {
      for (c2_i12 = 0; c2_i12 < 625; c2_i12 = c2_i12 + 1) {
        c2_mt[c2_i12] = 0U;
      }

      c2_seed = 5489.0;
      for (c2_i13 = 0; c2_i13 < 625; c2_i13 = c2_i13 + 1) {
        chartInstance.c2_twister_state[c2_i13] = c2_mt[c2_i13];
      }

      c2_Nminus1 = 624.0;
      c2_INIT_FACTOR = 1812433253U;
      c2_d4 = c2_seed;
      if (c2_d4 < 4.294967296E+09) {
        if (c2_d4 >= 0.0) {
          c2_u0 = (uint32_T)c2_d4;
        } else {
          c2_u0 = 0U;
        }
      } else {
        c2_u0 = MAX_uint32_T;
      }

      c2_b_r = c2_u0;
      chartInstance.c2_twister_state[0] = c2_b_r;
      c2_d5 = 2.0;
      for (c2_mti = c2_d5; c2_mti <= 624.0; c2_mti = c2_mti + 1.0) {
        c2_b_mti = c2_mti;
        c2_d6 = c2_b_mti - 1.0;
        c2_d7 = c2_d6 < 0.0 ? ceil(c2_d6 - 0.5) : floor(c2_d6 + 0.5);
        if (c2_d7 < 4.294967296E+09) {
          if (c2_d7 >= 0.0) {
            c2_u1 = (uint32_T)c2_d7;
          } else {
            c2_u1 = 0U;
          }
        } else {
          c2_u1 = MAX_uint32_T;
        }

        c2_b_r = (c2_b_r ^ c2_b_r >> 30U) * c2_INIT_FACTOR + c2_u1;
        chartInstance.c2_twister_state[(int32_T)_SFD_EML_ARRAY_BOUNDS_CHECK("mt",
          (int32_T)_SFD_INTEGER_CHECK("mti", c2_b_mti), 1, 625, 1, 0
          ) - 1] = c2_b_r;
      }

      c2_d8 = c2_Nminus1;
      c2_d9 = c2_d8 < 0.0 ? ceil(c2_d8 - 0.5) : floor(c2_d8 + 0.5);
      if (c2_d9 < 4.294967296E+09) {
        if (c2_d9 >= 0.0) {
          c2_u2 = (uint32_T)c2_d9;
        } else {
          c2_u2 = 0U;
        }
      } else {
        c2_u2 = MAX_uint32_T;
      }

      chartInstance.c2_twister_state[624] = c2_u2;
      chartInstance.c2_twister_state_not_empty = true;
    }

    for (c2_i14 = 0; c2_i14 < 625; c2_i14 = c2_i14 + 1) {
      c2_state[c2_i14] = chartInstance.c2_twister_state[c2_i14];
    }

    for (c2_i15 = 0; c2_i15 < 625; c2_i15 = c2_i15 + 1) {
      c2_b_twister_state[c2_i15] = c2_state[c2_i15];
    }

    do {
      for (c2_i16 = 0; c2_i16 < 625; c2_i16 = c2_i16 + 1) {
        c2_b_mt[c2_i16] = c2_b_twister_state[c2_i16];
      }

      for (c2_i17 = 0; c2_i17 < 625; c2_i17 = c2_i17 + 1) {
        c2_b_state[c2_i17] = c2_b_mt[c2_i17];
      }

      c2_N = 625.0;
      c2_M = 397.0;
      c2_NminusM = 228.0;
      c2_ONE = 1U;
      c2_UPPER_MASK = 2147483648U;
      c2_LOWER_MASK = 2147483647U;
      for (c2_i18 = 0; c2_i18 < 2; c2_i18 = c2_i18 + 1) {
        c2_u[c2_i18] = 0U;
      }

      c2_d10 = 1.0;
      for (c2_j = c2_d10; c2_j <= 2.0; c2_j = c2_j + 1.0) {
        c2_b_j = c2_j;
        c2_c_mti = c2_b_state[624] + c2_ONE;
        if ((real_T)c2_c_mti >= c2_N) {
          c2_d11 = 1.0;
          for (c2_kk = c2_d11; c2_kk <= 227.0; c2_kk = c2_kk + 1.0) {
            c2_b_kk = c2_kk;
            c2_y = (c2_b_state[(int32_T)_SFD_EML_ARRAY_BOUNDS_CHECK("mt",
                     (int32_T)_SFD_INTEGER_CHECK("kk", c2_b_kk), 1, 625, 1, 0) -
                    1] &
                    c2_UPPER_MASK) | (c2_b_state[(int32_T)
              _SFD_EML_ARRAY_BOUNDS_CHECK("mt", (int32_T)_SFD_INTEGER_CHECK("",
              c2_b_kk + 1.0), 1, 625, 1, 0
              ) - 1] & c2_LOWER_MASK);
            c2_b_y = c2_y;
            c2_c_y = c2_b_y;
            c2_b_ONE = 1U;
            if ((real_T)(c2_c_y & c2_b_ONE) == 0.0) {
              c2_c_y = c2_c_y >> _SFD_EML_ARRAY_BOUNDS_CHECK("y", (int32_T)
                _SFD_INTEGER_CHECK("ONE", (real_T)c2_b_ONE), 0, 31, 1, 1);
            } else {
              c2_c_y = c2_c_y >> _SFD_EML_ARRAY_BOUNDS_CHECK("y", (int32_T)
                _SFD_INTEGER_CHECK("ONE", (real_T)c2_b_ONE), 0, 31, 1, 1) ^
                2567483615U;
            }

            c2_b_state[(int32_T)_SFD_EML_ARRAY_BOUNDS_CHECK("mt", (int32_T)
              _SFD_INTEGER_CHECK("kk", c2_b_kk), 1, 625, 1, 0) - 1] =
              c2_b_state[(
                          int32_T)_SFD_EML_ARRAY_BOUNDS_CHECK("mt", (int32_T)
              _SFD_INTEGER_CHECK("", c2_b_kk + c2_M), 1, 625, 1, 0) - 1] ^
              c2_c_y;
          }

          c2_d12 = 228.0;
          for (c2_c_kk = c2_d12; c2_c_kk <= 623.0; c2_c_kk = c2_c_kk + 1.0) {
            c2_b_kk = c2_c_kk;
            c2_y = (c2_b_state[(int32_T)_SFD_EML_ARRAY_BOUNDS_CHECK("mt",
                     (int32_T)_SFD_INTEGER_CHECK("kk", c2_b_kk), 1, 625, 1, 0) -
                    1] &
                    c2_UPPER_MASK) | (c2_b_state[(int32_T)
              _SFD_EML_ARRAY_BOUNDS_CHECK("mt", (int32_T)_SFD_INTEGER_CHECK("",
              c2_b_kk + 1.0), 1, 625, 1, 0
              ) - 1] & c2_LOWER_MASK);
            c2_d_y = c2_y;
            c2_e_y = c2_d_y;
            c2_c_ONE = 1U;
            if ((real_T)(c2_e_y & c2_c_ONE) == 0.0) {
              c2_e_y = c2_e_y >> _SFD_EML_ARRAY_BOUNDS_CHECK("y", (int32_T)
                _SFD_INTEGER_CHECK("ONE", (real_T)c2_c_ONE), 0, 31, 1, 1);
            } else {
              c2_e_y = c2_e_y >> _SFD_EML_ARRAY_BOUNDS_CHECK("y", (int32_T)
                _SFD_INTEGER_CHECK("ONE", (real_T)c2_c_ONE), 0, 31, 1, 1) ^
                2567483615U;
            }

            c2_b_state[(int32_T)_SFD_EML_ARRAY_BOUNDS_CHECK("mt", (int32_T)
              _SFD_INTEGER_CHECK("kk", c2_b_kk), 1, 625, 1, 0) - 1] =
              c2_b_state[(
                          int32_T)_SFD_EML_ARRAY_BOUNDS_CHECK("mt", (int32_T)
              _SFD_INTEGER_CHECK("", (c2_b_kk + 1.0) - c2_NminusM), 1, 625, 1, 0)
              - 1] ^ c2_e_y;
          }

          c2_y = (c2_b_state[623] & c2_UPPER_MASK) | (c2_b_state[0] &
            c2_LOWER_MASK);
          c2_f_y = c2_y;
          c2_g_y = c2_f_y;
          c2_d_ONE = 1U;
          if ((real_T)(c2_g_y & c2_d_ONE) == 0.0) {
            c2_g_y = c2_g_y >> _SFD_EML_ARRAY_BOUNDS_CHECK("y", (int32_T)
              _SFD_INTEGER_CHECK("ONE", (real_T)c2_d_ONE), 0, 31, 1, 1);
          } else {
            c2_g_y = c2_g_y >> _SFD_EML_ARRAY_BOUNDS_CHECK("y", (int32_T)
              _SFD_INTEGER_CHECK("ONE", (real_T)c2_d_ONE), 0, 31, 1, 1) ^
              2567483615U;
          }

          c2_b_state[623] = c2_b_state[396] ^ c2_g_y;
          c2_c_mti = c2_ONE;
        }

        c2_y = c2_b_state[(int32_T)_SFD_EML_ARRAY_BOUNDS_CHECK("mt", (int32_T)
          _SFD_INTEGER_CHECK("mti", (real_T)c2_c_mti), 1, 625, 1, 0) - 1
          ];
        c2_b_state[624] = c2_c_mti;
        c2_y = c2_y ^ c2_y >> 11U;
        c2_y = c2_y ^ (c2_y << 7U & 2636928640U);
        c2_y = c2_y ^ (c2_y << 15U & 4022730752U);
        c2_y = c2_y ^ c2_y >> 18U;
        c2_u[(int32_T)_SFD_EML_ARRAY_BOUNDS_CHECK("u", (int32_T)
          _SFD_INTEGER_CHECK("j", c2_b_j), 1, 2, 1, 0) - 1] = c2_y;
      }

      for (c2_i19 = 0; c2_i19 < 625; c2_i19 = c2_i19 + 1) {
        c2_b_twister_state[c2_i19] = c2_b_state[c2_i19];
      }

      for (c2_i20 = 0; c2_i20 < 2; c2_i20 = c2_i20 + 1) {
        c2_b_u[c2_i20] = c2_u[c2_i20];
      }

      c2_b_u[0] = c2_b_u[0] >> 5U;
      c2_b_u[1] = c2_b_u[1] >> 6U;
      c2_a = (real_T)c2_b_u[0];
      c2_b = 6.7108864E+07;
      c2_h_y = c2_a * c2_b;
      c2_b_b = c2_h_y + (real_T)c2_b_u[1];
      c2_b_a = 1.1102230246251565E-16;
      c2_d13 = c2_b_a * c2_b_b;
    } while (!(c2_d13 != 0.0));

    for (c2_i21 = 0; c2_i21 < 625; c2_i21 = c2_i21 + 1) {
      chartInstance.c2_twister_state[c2_i21] = c2_b_twister_state[c2_i21];
    }

    return c2_d13;
  } else {
    if (!chartInstance.c2_v4_state_not_empty) {
      chartInstance.c2_v4_state = 1144108930U;
      chartInstance.c2_v4_state_not_empty = true;
    }

    c2_s = chartInstance.c2_v4_state;
    c2_IA = 16807U;
    c2_IM = 2147483647U;
    c2_IQ = 127773U;
    c2_IR = 2836U;
    c2_u3 = c2_IQ;
    c2_hi = c2_u3 == (uint32_T)0 ? MAX_uint32_T : (uint32_T)(c2_s / c2_u3);
    c2_lo = c2_s - c2_hi * c2_IQ;
    c2_test1 = c2_IA * c2_lo;
    c2_test2 = c2_IR * c2_hi;
    if (c2_test1 < c2_test2) {
      c2_b_v4_state = (c2_IM - c2_test2) + c2_test1;
    } else {
      c2_b_v4_state = c2_test1 - c2_test2;
    }

    c2_c_a = (real_T)c2_b_v4_state;
    c2_c_b = 4.6566128752457969E-10;
    c2_d14 = c2_c_a * c2_c_b;
    chartInstance.c2_v4_state = c2_b_v4_state;
    return c2_d14;
  }
}

static const mxArray *c2_sf_marshall(void *c2_chartInstance, void *c2_u)
{
  const mxArray *c2_y = NULL;
  real_T c2_b_u;
  const mxArray *c2_b_y = NULL;
  c2_y = NULL;
  c2_b_u = *((real_T *)c2_u);
  c2_b_y = NULL;
  sf_mex_assign(&c2_b_y, sf_mex_create(&c2_b_u, "y", 0, 0U, 0U, 0));
  sf_mex_assign(&c2_y, c2_b_y);
  return c2_y;
}

static const mxArray *c2_b_sf_marshall(void *c2_chartInstance, void *c2_u)
{
  const mxArray *c2_y = NULL;
  int32_T c2_i22;
  int32_T c2_i23;
  real_T c2_b_u[4];
  const mxArray *c2_b_y = NULL;
  c2_y = NULL;
  for (c2_i22 = 0; c2_i22 < 2; c2_i22 = c2_i22 + 1) {
    for (c2_i23 = 0; c2_i23 < 2; c2_i23 = c2_i23 + 1) {
      c2_b_u[c2_i23 + 2 * c2_i22] = (*((real_T (*)[4])c2_u))[c2_i23 + 2 * c2_i22];
    }
  }

  c2_b_y = NULL;
  sf_mex_assign(&c2_b_y, sf_mex_create(&c2_b_u, "y", 0, 0U, 1U, 2, 2, 2));
  sf_mex_assign(&c2_y, c2_b_y);
  return c2_y;
}

static const mxArray *c2_c_sf_marshall(void *c2_chartInstance, void *c2_u)
{
  const mxArray *c2_y = NULL;
  int32_T c2_i24;
  real_T c2_b_u[2];
  const mxArray *c2_b_y = NULL;
  c2_y = NULL;
  for (c2_i24 = 0; c2_i24 < 2; c2_i24 = c2_i24 + 1) {
    c2_b_u[c2_i24] = (*((real_T (*)[2])c2_u))[c2_i24];
  }

  c2_b_y = NULL;
  sf_mex_assign(&c2_b_y, sf_mex_create(&c2_b_u, "y", 0, 0U, 1U, 2, 1, 2));
  sf_mex_assign(&c2_y, c2_b_y);
  return c2_y;
}

static const mxArray *c2_d_sf_marshall(void *c2_chartInstance, void *c2_u)
{
  const mxArray *c2_y = NULL;
  int32_T c2_i25;
  real_T c2_b_u[2];
  const mxArray *c2_b_y = NULL;
  c2_y = NULL;
  for (c2_i25 = 0; c2_i25 < 2; c2_i25 = c2_i25 + 1) {
    c2_b_u[c2_i25] = (*((real_T (*)[2])c2_u))[c2_i25];
  }

  c2_b_y = NULL;
  sf_mex_assign(&c2_b_y, sf_mex_create(&c2_b_u, "y", 0, 0U, 1U, 1, 2));
  sf_mex_assign(&c2_y, c2_b_y);
  return c2_y;
}

const mxArray *sf_c2_conmutador_get_eml_resolved_functions_info(void)
{
  const mxArray *c2_nameCaptureInfo = NULL;
  c2_ResolvedFunctionInfo c2_info[56];
  const mxArray *c2_m0 = NULL;
  int32_T c2_i26;
  c2_ResolvedFunctionInfo *c2_r0;
  c2_nameCaptureInfo = NULL;
  c2_info_helper(c2_info);
  sf_mex_assign(&c2_m0, sf_mex_createstruct("nameCaptureInfo", 1, 56));
  for (c2_i26 = 0; c2_i26 < 56; c2_i26 = c2_i26 + 1) {
    c2_r0 = &c2_info[c2_i26];
    sf_mex_addfield(c2_m0, sf_mex_create(c2_r0->context, "nameCaptureInfo", 13,
      0U, 0U, 2, 1, strlen(c2_r0->context)), "context",
                    "nameCaptureInfo", c2_i26);
    sf_mex_addfield(c2_m0, sf_mex_create(c2_r0->name, "nameCaptureInfo", 13, 0U,
      0U, 2, 1, strlen(c2_r0->name)), "name",
                    "nameCaptureInfo", c2_i26);
    sf_mex_addfield(c2_m0, sf_mex_create(c2_r0->dominantType, "nameCaptureInfo",
      13, 0U, 0U, 2, 1, strlen(c2_r0->dominantType)),
                    "dominantType", "nameCaptureInfo", c2_i26);
    sf_mex_addfield(c2_m0, sf_mex_create(c2_r0->resolved, "nameCaptureInfo", 13,
      0U, 0U, 2, 1, strlen(c2_r0->resolved)), "resolved",
                    "nameCaptureInfo", c2_i26);
    sf_mex_addfield(c2_m0, sf_mex_create(&c2_r0->fileLength, "nameCaptureInfo",
      7, 0U, 0U, 0), "fileLength", "nameCaptureInfo", c2_i26);
    sf_mex_addfield(c2_m0, sf_mex_create(&c2_r0->fileTime1, "nameCaptureInfo", 7,
      0U, 0U, 0), "fileTime1", "nameCaptureInfo", c2_i26);
    sf_mex_addfield(c2_m0, sf_mex_create(&c2_r0->fileTime2, "nameCaptureInfo", 7,
      0U, 0U, 0), "fileTime2", "nameCaptureInfo", c2_i26);
  }

  sf_mex_assign(&c2_nameCaptureInfo, c2_m0);
  return c2_nameCaptureInfo;
}

static void c2_info_helper(c2_ResolvedFunctionInfo c2_info[56])
{
  c2_info[0].context =
    "[I]$matlabroot$/toolbox/eml/lib/matlab/datafun/sum.m/sumwork";
  c2_info[0].name = "cast";
  c2_info[0].dominantType = "double";
  c2_info[0].resolved =
    "[I]$matlabroot$/toolbox/eml/lib/matlab/datatypes/cast.m";
  c2_info[0].fileLength = 866U;
  c2_info[0].fileTime1 = 1192488243U;
  c2_info[0].fileTime2 = 0U;
  c2_info[1].context =
    "[I]$matlabroot$/toolbox/eml/lib/matlab/eml/eml_check_dim.m";
  c2_info[1].name = "isreal";
  c2_info[1].dominantType = "double";
  c2_info[1].resolved = "[B]isreal";
  c2_info[1].fileLength = 0U;
  c2_info[1].fileTime1 = 0U;
  c2_info[1].fileTime2 = 0U;
  c2_info[2].context = "[I]$matlabroot$/toolbox/eml/lib/matlab/elmat/length.m";
  c2_info[2].name = "size";
  c2_info[2].dominantType = "double";
  c2_info[2].resolved = "[B]size";
  c2_info[2].fileLength = 0U;
  c2_info[2].fileTime1 = 0U;
  c2_info[2].fileTime2 = 0U;
  c2_info[3].context = "[I]$matlabroot$/toolbox/eml/lib/matlab/elmat/length.m";
  c2_info[3].name = "nargin";
  c2_info[3].dominantType = "";
  c2_info[3].resolved = "[B]nargin";
  c2_info[3].fileLength = 0U;
  c2_info[3].fileTime1 = 0U;
  c2_info[3].fileTime2 = 0U;
  c2_info[4].context = "";
  c2_info[4].name = "ne";
  c2_info[4].dominantType = "double";
  c2_info[4].resolved = "[B]ne";
  c2_info[4].fileLength = 0U;
  c2_info[4].fileTime1 = 0U;
  c2_info[4].fileTime2 = 0U;
  c2_info[5].context =
    "[I]$matlabroot$/toolbox/eml/lib/matlab/eml/eml_all_or_any.m";
  c2_info[5].name = "eml_nonsingleton_dim";
  c2_info[5].dominantType = "logical";
  c2_info[5].resolved =
    "[I]$matlabroot$/toolbox/eml/lib/matlab/eml/eml_nonsingleton_dim.m";
  c2_info[5].fileLength = 404U;
  c2_info[5].fileTime1 = 1192488399U;
  c2_info[5].fileTime2 = 0U;
  c2_info[6].context = "[I]$matlabroot$/toolbox/eml/lib/matlab/elmat/rand.m";
  c2_info[6].name = "eml_randtwister";
  c2_info[6].dominantType = "char";
  c2_info[6].resolved =
    "[I]$matlabroot$/toolbox/eml/lib/matlab/eml/eml_randtwister.m";
  c2_info[6].fileLength = 8456U;
  c2_info[6].fileTime1 = 1192488402U;
  c2_info[6].fileTime2 = 0U;
  c2_info[7].context =
    "[I]$matlabroot$/toolbox/eml/lib/matlab/eml/eml_check_dim.m";
  c2_info[7].name = "le";
  c2_info[7].dominantType = "int32";
  c2_info[7].resolved = "[B]le";
  c2_info[7].fileLength = 0U;
  c2_info[7].fileTime1 = 0U;
  c2_info[7].fileTime2 = 0U;
  c2_info[8].context = "[I]$matlabroot$/toolbox/eml/lib/matlab/elmat/intmax.m";
  c2_info[8].name = "not";
  c2_info[8].dominantType = "logical";
  c2_info[8].resolved = "[B]not";
  c2_info[8].fileLength = 0U;
  c2_info[8].fileTime1 = 0U;
  c2_info[8].fileTime2 = 0U;
  c2_info[9].context = "[I]$matlabroot$/toolbox/eml/lib/matlab/ops/all.m";
  c2_info[9].name = "ischar";
  c2_info[9].dominantType = "logical";
  c2_info[9].resolved = "[B]ischar";
  c2_info[9].fileLength = 0U;
  c2_info[9].fileTime1 = 0U;
  c2_info[9].fileTime2 = 0U;
  c2_info[10].context =
    "[I]$matlabroot$/toolbox/eml/lib/matlab/datatypes/cast.m";
  c2_info[10].name = "double";
  c2_info[10].dominantType = "double";
  c2_info[10].resolved = "[B]double";
  c2_info[10].fileLength = 0U;
  c2_info[10].fileTime1 = 0U;
  c2_info[10].fileTime2 = 0U;
  c2_info[11].context =
    "[I]$matlabroot$/toolbox/eml/lib/matlab/elmat/isequal.m/binary_isequal";
  c2_info[11].name = "all";
  c2_info[11].dominantType = "logical";
  c2_info[11].resolved = "[I]$matlabroot$/toolbox/eml/lib/matlab/ops/all.m";
  c2_info[11].fileLength = 400U;
  c2_info[11].fileTime1 = 1192488504U;
  c2_info[11].fileTime2 = 0U;
  c2_info[12].context =
    "[I]$matlabroot$/toolbox/eml/lib/matlab/eml/eml_check_dim.m";
  c2_info[12].name = "isscalar";
  c2_info[12].dominantType = "double";
  c2_info[12].resolved = "[B]isscalar";
  c2_info[12].fileLength = 0U;
  c2_info[12].fileTime1 = 0U;
  c2_info[12].fileTime2 = 0U;
  c2_info[13].context = "[I]$matlabroot$/toolbox/eml/lib/matlab/elmat/length.m";
  c2_info[13].name = "eq";
  c2_info[13].dominantType = "double";
  c2_info[13].resolved = "[B]eq";
  c2_info[13].fileLength = 0U;
  c2_info[13].fileTime1 = 0U;
  c2_info[13].fileTime2 = 0U;
  c2_info[14].context = "[I]$matlabroot$/toolbox/eml/lib/matlab/elmat/isequal.m";
  c2_info[14].name = "class";
  c2_info[14].dominantType = "double";
  c2_info[14].resolved = "[B]class";
  c2_info[14].fileLength = 0U;
  c2_info[14].fileTime1 = 0U;
  c2_info[14].fileTime2 = 0U;
  c2_info[15].context = "[I]$matlabroot$/toolbox/eml/lib/matlab/elfun/floor.m";
  c2_info[15].name = "isinteger";
  c2_info[15].dominantType = "double";
  c2_info[15].resolved = "[B]isinteger";
  c2_info[15].fileLength = 0U;
  c2_info[15].fileTime1 = 0U;
  c2_info[15].fileTime2 = 0U;
  c2_info[16].context = "[I]$matlabroot$/toolbox/eml/lib/matlab/elmat/rand.m";
  c2_info[16].name = "eml_varargin_to_size";
  c2_info[16].dominantType = "double";
  c2_info[16].resolved =
    "[I]$matlabroot$/toolbox/eml/lib/matlab/eml/eml_varargin_to_size.m";
  c2_info[16].fileLength = 625U;
  c2_info[16].fileTime1 = 1192488412U;
  c2_info[16].fileTime2 = 0U;
  c2_info[17].context =
    "[I]$matlabroot$/toolbox/eml/lib/matlab/eml/eml_all_or_any.m";
  c2_info[17].name = "eml_set_singleton_dim";
  c2_info[17].dominantType = "double";
  c2_info[17].resolved =
    "[I]$matlabroot$/toolbox/eml/lib/matlab/eml/eml_set_singleton_dim.m";
  c2_info[17].fileLength = 375U;
  c2_info[17].fileTime1 = 1192488406U;
  c2_info[17].fileTime2 = 0U;
  c2_info[18].context =
    "[I]$matlabroot$/toolbox/eml/lib/matlab/eml/eml_randtwister.m";
  c2_info[18].name = "mtimes";
  c2_info[18].dominantType = "double";
  c2_info[18].resolved = "[I]$matlabroot$/toolbox/eml/lib/matlab/ops/mtimes.m";
  c2_info[18].fileLength = 1990U;
  c2_info[18].fileTime1 = 1197673949U;
  c2_info[18].fileTime2 = 0U;
  c2_info[19].context = "[I]$matlabroot$/toolbox/eml/lib/matlab/elmat/eps.m";
  c2_info[19].name = "eml_is_float_class";
  c2_info[19].dominantType = "char";
  c2_info[19].resolved =
    "[I]$matlabroot$/toolbox/eml/lib/matlab/eml/eml_is_float_class.m";
  c2_info[19].fileLength = 226U;
  c2_info[19].fileTime1 = 1197673949U;
  c2_info[19].fileTime2 = 0U;
  c2_info[20].context = "";
  c2_info[20].name = "sum";
  c2_info[20].dominantType = "double";
  c2_info[20].resolved = "[I]$matlabroot$/toolbox/eml/lib/matlab/datafun/sum.m";
  c2_info[20].fileLength = 2571U;
  c2_info[20].fileTime1 = 1192488240U;
  c2_info[20].fileTime2 = 0U;
  c2_info[21].context =
    "[I]$matlabroot$/toolbox/eml/lib/matlab/datafun/sum.m/sumwork";
  c2_info[21].name = "isvector";
  c2_info[21].dominantType = "double";
  c2_info[21].resolved = "[B]isvector";
  c2_info[21].fileLength = 0U;
  c2_info[21].fileTime1 = 0U;
  c2_info[21].fileTime2 = 0U;
  c2_info[22].context = "";
  c2_info[22].name = "zeros";
  c2_info[22].dominantType = "double";
  c2_info[22].resolved = "[B]zeros";
  c2_info[22].fileLength = 0U;
  c2_info[22].fileTime1 = 0U;
  c2_info[22].fileTime2 = 0U;
  c2_info[23].context = "[I]$matlabroot$/toolbox/eml/lib/matlab/elmat/isequal.m";
  c2_info[23].name = "isa";
  c2_info[23].dominantType = "double";
  c2_info[23].resolved = "[B]isa";
  c2_info[23].fileLength = 0U;
  c2_info[23].fileTime1 = 0U;
  c2_info[23].fileTime2 = 0U;
  c2_info[24].context = "[I]$matlabroot$/toolbox/eml/lib/matlab/elmat/isequal.m";
  c2_info[24].name = "ge";
  c2_info[24].dominantType = "double";
  c2_info[24].resolved = "[B]ge";
  c2_info[24].fileLength = 0U;
  c2_info[24].fileTime1 = 0U;
  c2_info[24].fileTime2 = 0U;
  c2_info[25].context = "[I]$matlabroot$/toolbox/eml/lib/matlab/elmat/intmax.m";
  c2_info[25].name = "int32";
  c2_info[25].dominantType = "double";
  c2_info[25].resolved = "[B]int32";
  c2_info[25].fileLength = 0U;
  c2_info[25].fileTime1 = 0U;
  c2_info[25].fileTime2 = 0U;
  c2_info[26].context = "[I]$matlabroot$/toolbox/eml/lib/matlab/ops/all.m";
  c2_info[26].name = "eml_all_or_any";
  c2_info[26].dominantType = "char";
  c2_info[26].resolved =
    "[I]$matlabroot$/toolbox/eml/lib/matlab/eml/eml_all_or_any.m";
  c2_info[26].fileLength = 3266U;
  c2_info[26].fileTime1 = 1192488364U;
  c2_info[26].fileTime2 = 0U;
  c2_info[27].context =
    "[I]$matlabroot$/toolbox/eml/lib/matlab/eml/eml_check_dim.m";
  c2_info[27].name = "floor";
  c2_info[27].dominantType = "double";
  c2_info[27].resolved = "[I]$matlabroot$/toolbox/eml/lib/matlab/elfun/floor.m";
  c2_info[27].fileLength = 550U;
  c2_info[27].fileTime1 = 1192488285U;
  c2_info[27].fileTime2 = 0U;
  c2_info[28].context = "[I]$matlabroot$/toolbox/eml/lib/matlab/elmat/eps.m";
  c2_info[28].name = "uminus";
  c2_info[28].dominantType = "double";
  c2_info[28].resolved = "[B]uminus";
  c2_info[28].fileLength = 0U;
  c2_info[28].fileTime1 = 0U;
  c2_info[28].fileTime2 = 0U;
  c2_info[29].context = "[I]$matlabroot$/toolbox/eml/lib/matlab/elmat/length.m";
  c2_info[29].name = "gt";
  c2_info[29].dominantType = "double";
  c2_info[29].resolved = "[B]gt";
  c2_info[29].fileLength = 0U;
  c2_info[29].fileTime1 = 0U;
  c2_info[29].fileTime2 = 0U;
  c2_info[30].context = "[I]$matlabroot$/toolbox/eml/lib/matlab/ops/all.m";
  c2_info[30].name = "lt";
  c2_info[30].dominantType = "double";
  c2_info[30].resolved = "[B]lt";
  c2_info[30].fileLength = 0U;
  c2_info[30].fileTime1 = 0U;
  c2_info[30].fileTime2 = 0U;
  c2_info[31].context =
    "[I]$matlabroot$/toolbox/eml/lib/matlab/eml/eml_all_or_any.m";
  c2_info[31].name = "true";
  c2_info[31].dominantType = "";
  c2_info[31].resolved = "[I]$matlabroot$/toolbox/eml/lib/matlab/elmat/true.m";
  c2_info[31].fileLength = 237U;
  c2_info[31].fileTime1 = 1192488361U;
  c2_info[31].fileTime2 = 0U;
  c2_info[32].context =
    "[I]$matlabroot$/toolbox/eml/lib/matlab/eml/eml_check_dim.m";
  c2_info[32].name = "nargout";
  c2_info[32].dominantType = "";
  c2_info[32].resolved = "[B]nargout";
  c2_info[32].fileLength = 0U;
  c2_info[32].fileTime1 = 0U;
  c2_info[32].fileTime2 = 0U;
  c2_info[33].context =
    "[I]$matlabroot$/toolbox/eml/lib/matlab/eml/eml_assert_valid_size_arg.m";
  c2_info[33].name = "isnumeric";
  c2_info[33].dominantType = "double";
  c2_info[33].resolved = "[B]isnumeric";
  c2_info[33].fileLength = 0U;
  c2_info[33].fileTime1 = 0U;
  c2_info[33].fileTime2 = 0U;
  c2_info[34].context = "[I]$matlabroot$/toolbox/eml/lib/matlab/elmat/false.m";
  c2_info[34].name = "logical";
  c2_info[34].dominantType = "double";
  c2_info[34].resolved = "[B]logical";
  c2_info[34].fileLength = 0U;
  c2_info[34].fileTime1 = 0U;
  c2_info[34].fileTime2 = 0U;
  c2_info[35].context = "[I]$matlabroot$/toolbox/eml/lib/matlab/elmat/rand.m";
  c2_info[35].name = "uint8";
  c2_info[35].dominantType = "double";
  c2_info[35].resolved = "[B]uint8";
  c2_info[35].fileLength = 0U;
  c2_info[35].fileTime1 = 0U;
  c2_info[35].fileTime2 = 0U;
  c2_info[36].context = "";
  c2_info[36].name = "length";
  c2_info[36].dominantType = "double";
  c2_info[36].resolved = "[I]$matlabroot$/toolbox/eml/lib/matlab/elmat/length.m";
  c2_info[36].fileLength = 303U;
  c2_info[36].fileTime1 = 1192488338U;
  c2_info[36].fileTime2 = 0U;
  c2_info[37].context =
    "[I]$matlabroot$/toolbox/eml/lib/matlab/eml/eml_all_or_any.m";
  c2_info[37].name = "strcmp";
  c2_info[37].dominantType = "char";
  c2_info[37].resolved = "[B]strcmp";
  c2_info[37].fileLength = 0U;
  c2_info[37].fileTime1 = 0U;
  c2_info[37].fileTime2 = 0U;
  c2_info[38].context =
    "[I]$matlabroot$/toolbox/eml/lib/matlab/eml/eml_all_or_any.m";
  c2_info[38].name = "isempty";
  c2_info[38].dominantType = "logical";
  c2_info[38].resolved = "[B]isempty";
  c2_info[38].fileLength = 0U;
  c2_info[38].fileTime1 = 0U;
  c2_info[38].fileTime2 = 0U;
  c2_info[39].context = "";
  c2_info[39].name = "rand";
  c2_info[39].dominantType = "double";
  c2_info[39].resolved = "[I]$matlabroot$/toolbox/eml/lib/matlab/elmat/rand.m";
  c2_info[39].fileLength = 3050U;
  c2_info[39].fileTime1 = 1192488348U;
  c2_info[39].fileTime2 = 0U;
  c2_info[40].context = "[I]$matlabroot$/toolbox/eml/lib/matlab/datafun/sum.m";
  c2_info[40].name = "isequal";
  c2_info[40].dominantType = "double";
  c2_info[40].resolved =
    "[I]$matlabroot$/toolbox/eml/lib/matlab/elmat/isequal.m";
  c2_info[40].fileLength = 2029U;
  c2_info[40].fileTime1 = 1192488332U;
  c2_info[40].fileTime2 = 0U;
  c2_info[41].context = "[I]$matlabroot$/toolbox/eml/lib/matlab/elmat/isequal.m";
  c2_info[41].name = "false";
  c2_info[41].dominantType = "";
  c2_info[41].resolved = "[I]$matlabroot$/toolbox/eml/lib/matlab/elmat/false.m";
  c2_info[41].fileLength = 238U;
  c2_info[41].fileTime1 = 1192488320U;
  c2_info[41].fileTime2 = 0U;
  c2_info[42].context =
    "[I]$matlabroot$/toolbox/eml/lib/matlab/elmat/isequal.m/binary_isequal";
  c2_info[42].name = "ndims";
  c2_info[42].dominantType = "double";
  c2_info[42].resolved = "[B]ndims";
  c2_info[42].fileLength = 0U;
  c2_info[42].fileTime1 = 0U;
  c2_info[42].fileTime2 = 0U;
  c2_info[43].context = "";
  c2_info[43].name = "ones";
  c2_info[43].dominantType = "double";
  c2_info[43].resolved = "[B]ones";
  c2_info[43].fileLength = 0U;
  c2_info[43].fileTime1 = 0U;
  c2_info[43].fileTime2 = 0U;
  c2_info[44].context =
    "[I]$matlabroot$/toolbox/eml/lib/matlab/eml/eml_randtwister.m/twister_state_vector";
  c2_info[44].name = "minus";
  c2_info[44].dominantType = "double";
  c2_info[44].resolved = "[B]minus";
  c2_info[44].fileLength = 0U;
  c2_info[44].fileTime1 = 0U;
  c2_info[44].fileTime2 = 0U;
  c2_info[45].context =
    "[I]$matlabroot$/toolbox/eml/lib/matlab/eml/eml_check_dim.m";
  c2_info[45].name = "eml_index_class";
  c2_info[45].dominantType = "";
  c2_info[45].resolved =
    "[I]$matlabroot$/toolbox/eml/lib/matlab/eml/eml_index_class.m";
  c2_info[45].fileLength = 909U;
  c2_info[45].fileTime1 = 1192488382U;
  c2_info[45].fileTime2 = 0U;
  c2_info[46].context = "";
  c2_info[46].name = "ceil";
  c2_info[46].dominantType = "double";
  c2_info[46].resolved = "[I]$matlabroot$/toolbox/eml/lib/matlab/elfun/ceil.m";
  c2_info[46].fileLength = 530U;
  c2_info[46].fileTime1 = 1192488272U;
  c2_info[46].fileTime2 = 0U;
  c2_info[47].context =
    "[I]$matlabroot$/toolbox/eml/lib/matlab/eml/eml_randtwister.m";
  c2_info[47].name = "eps";
  c2_info[47].dominantType = "";
  c2_info[47].resolved = "[I]$matlabroot$/toolbox/eml/lib/matlab/elmat/eps.m";
  c2_info[47].fileLength = 1339U;
  c2_info[47].fileTime1 = 1192488318U;
  c2_info[47].fileTime2 = 0U;
  c2_info[48].context = "[I]$matlabroot$/toolbox/eml/lib/matlab/elmat/length.m";
  c2_info[48].name = "eml_assert";
  c2_info[48].dominantType = "char";
  c2_info[48].resolved = "[B]eml_assert";
  c2_info[48].fileLength = 0U;
  c2_info[48].fileTime1 = 0U;
  c2_info[48].fileTime2 = 0U;
  c2_info[49].context =
    "[I]$matlabroot$/toolbox/eml/lib/matlab/eml/eml_randtwister.m/twister_state_vector";
  c2_info[49].name = "uint32";
  c2_info[49].dominantType = "double";
  c2_info[49].resolved = "[B]uint32";
  c2_info[49].fileLength = 0U;
  c2_info[49].fileTime1 = 0U;
  c2_info[49].fileTime2 = 0U;
  c2_info[50].context = "[I]$matlabroot$/toolbox/eml/lib/matlab/elmat/rand.m";
  c2_info[50].name = "eml_randv4";
  c2_info[50].dominantType = "char";
  c2_info[50].resolved =
    "[I]$matlabroot$/toolbox/eml/lib/matlab/eml/eml_randv4.m";
  c2_info[50].fileLength = 3830U;
  c2_info[50].fileTime1 = 1192488403U;
  c2_info[50].fileTime2 = 0U;
  c2_info[51].context =
    "[I]$matlabroot$/toolbox/eml/lib/matlab/eml/eml_check_dim.m";
  c2_info[51].name = "intmax";
  c2_info[51].dominantType = "char";
  c2_info[51].resolved = "[I]$matlabroot$/toolbox/eml/lib/matlab/elmat/intmax.m";
  c2_info[51].fileLength = 1535U;
  c2_info[51].fileTime1 = 1192488328U;
  c2_info[51].fileTime2 = 0U;
  c2_info[52].context =
    "[I]$matlabroot$/toolbox/eml/lib/matlab/datafun/sum.m/sumwork";
  c2_info[52].name = "plus";
  c2_info[52].dominantType = "double";
  c2_info[52].resolved = "[B]plus";
  c2_info[52].fileLength = 0U;
  c2_info[52].fileTime1 = 0U;
  c2_info[52].fileTime2 = 0U;
  c2_info[53].context =
    "[I]$matlabroot$/toolbox/eml/lib/matlab/eml/eml_varargin_to_size.m";
  c2_info[53].name = "eml_assert_valid_size_arg";
  c2_info[53].dominantType = "double";
  c2_info[53].resolved =
    "[I]$matlabroot$/toolbox/eml/lib/matlab/eml/eml_assert_valid_size_arg.m";
  c2_info[53].fileLength = 1360U;
  c2_info[53].fileTime1 = 1192488366U;
  c2_info[53].fileTime2 = 0U;
  c2_info[54].context =
    "[I]$matlabroot$/toolbox/eml/lib/matlab/eml/eml_all_or_any.m";
  c2_info[54].name = "eml_check_dim";
  c2_info[54].dominantType = "double";
  c2_info[54].resolved =
    "[I]$matlabroot$/toolbox/eml/lib/matlab/eml/eml_check_dim.m";
  c2_info[54].fileLength = 1434U;
  c2_info[54].fileTime1 = 1192488368U;
  c2_info[54].fileTime2 = 0U;
  c2_info[55].context = "[I]$matlabroot$/toolbox/eml/lib/matlab/ops/all.m";
  c2_info[55].name = "islogical";
  c2_info[55].dominantType = "logical";
  c2_info[55].resolved = "[B]islogical";
  c2_info[55].fileLength = 0U;
  c2_info[55].fileTime1 = 0U;
  c2_info[55].fileTime2 = 0U;
}

static void init_dsm_address_info(void)
{
}

/* SFunction Glue Code */
void sf_c2_conmutador_get_check_sum(mxArray *plhs[])
{
  ((real_T *)mxGetPr((plhs[0])))[0] = (real_T)(2867806705U);
  ((real_T *)mxGetPr((plhs[0])))[1] = (real_T)(3303341615U);
  ((real_T *)mxGetPr((plhs[0])))[2] = (real_T)(3416240326U);
  ((real_T *)mxGetPr((plhs[0])))[3] = (real_T)(3872874075U);
}

mxArray *sf_c2_conmutador_get_autoinheritance_info(void)
{
  const char *autoinheritanceFields[] = { "checksum", "inputs", "parameters",
    "outputs" };

  mxArray *mxAutoinheritanceInfo = mxCreateStructMatrix(1,1,4,
    autoinheritanceFields);

  {
    mxArray *mxChecksum = mxCreateDoubleMatrix(4,1,mxREAL);
    double *pr = mxGetPr(mxChecksum);
    pr[0] = (double)(43732335U);
    pr[1] = (double)(4108564599U);
    pr[2] = (double)(2515138733U);
    pr[3] = (double)(2578654932U);
    mxSetField(mxAutoinheritanceInfo,0,"checksum",mxChecksum);
  }

  {
    const char *dataFields[] = { "size", "type", "complexity" };

    mxArray *mxData = mxCreateStructMatrix(1,2,3,dataFields);

    {
      mxArray *mxSize = mxCreateDoubleMatrix(1,2,mxREAL);
      double *pr = mxGetPr(mxSize);
      pr[0] = (double)(1);
      pr[1] = (double)(1);
      mxSetField(mxData,0,"size",mxSize);
    }

    {
      const char *typeFields[] = { "base", "aliasId", "fixpt" };

      mxArray *mxType = mxCreateStructMatrix(1,1,3,typeFields);
      mxSetField(mxType,0,"base",mxCreateDoubleScalar(10));
      mxSetField(mxType,0,"aliasId",mxCreateDoubleScalar(0));
      mxSetField(mxType,0,"fixpt",mxCreateDoubleMatrix(0,0,mxREAL));
      mxSetField(mxData,0,"type",mxType);
    }

    mxSetField(mxData,0,"complexity",mxCreateDoubleScalar(0));

    {
      mxArray *mxSize = mxCreateDoubleMatrix(1,2,mxREAL);
      double *pr = mxGetPr(mxSize);
      pr[0] = (double)(1);
      pr[1] = (double)(1);
      mxSetField(mxData,1,"size",mxSize);
    }

    {
      const char *typeFields[] = { "base", "aliasId", "fixpt" };

      mxArray *mxType = mxCreateStructMatrix(1,1,3,typeFields);
      mxSetField(mxType,0,"base",mxCreateDoubleScalar(10));
      mxSetField(mxType,0,"aliasId",mxCreateDoubleScalar(0));
      mxSetField(mxType,0,"fixpt",mxCreateDoubleMatrix(0,0,mxREAL));
      mxSetField(mxData,1,"type",mxType);
    }

    mxSetField(mxData,1,"complexity",mxCreateDoubleScalar(0));
    mxSetField(mxAutoinheritanceInfo,0,"inputs",mxData);
  }

  {
    mxSetField(mxAutoinheritanceInfo,0,"parameters",mxCreateDoubleMatrix(0,0,
                mxREAL));
  }

  {
    const char *dataFields[] = { "size", "type", "complexity" };

    mxArray *mxData = mxCreateStructMatrix(1,4,3,dataFields);

    {
      mxArray *mxSize = mxCreateDoubleMatrix(1,2,mxREAL);
      double *pr = mxGetPr(mxSize);
      pr[0] = (double)(1);
      pr[1] = (double)(1);
      mxSetField(mxData,0,"size",mxSize);
    }

    {
      const char *typeFields[] = { "base", "aliasId", "fixpt" };

      mxArray *mxType = mxCreateStructMatrix(1,1,3,typeFields);
      mxSetField(mxType,0,"base",mxCreateDoubleScalar(10));
      mxSetField(mxType,0,"aliasId",mxCreateDoubleScalar(0));
      mxSetField(mxType,0,"fixpt",mxCreateDoubleMatrix(0,0,mxREAL));
      mxSetField(mxData,0,"type",mxType);
    }

    mxSetField(mxData,0,"complexity",mxCreateDoubleScalar(0));

    {
      mxArray *mxSize = mxCreateDoubleMatrix(1,2,mxREAL);
      double *pr = mxGetPr(mxSize);
      pr[0] = (double)(1);
      pr[1] = (double)(1);
      mxSetField(mxData,1,"size",mxSize);
    }

    {
      const char *typeFields[] = { "base", "aliasId", "fixpt" };

      mxArray *mxType = mxCreateStructMatrix(1,1,3,typeFields);
      mxSetField(mxType,0,"base",mxCreateDoubleScalar(10));
      mxSetField(mxType,0,"aliasId",mxCreateDoubleScalar(0));
      mxSetField(mxType,0,"fixpt",mxCreateDoubleMatrix(0,0,mxREAL));
      mxSetField(mxData,1,"type",mxType);
    }

    mxSetField(mxData,1,"complexity",mxCreateDoubleScalar(0));

    {
      mxArray *mxSize = mxCreateDoubleMatrix(1,2,mxREAL);
      double *pr = mxGetPr(mxSize);
      pr[0] = (double)(1);
      pr[1] = (double)(1);
      mxSetField(mxData,2,"size",mxSize);
    }

    {
      const char *typeFields[] = { "base", "aliasId", "fixpt" };

      mxArray *mxType = mxCreateStructMatrix(1,1,3,typeFields);
      mxSetField(mxType,0,"base",mxCreateDoubleScalar(10));
      mxSetField(mxType,0,"aliasId",mxCreateDoubleScalar(0));
      mxSetField(mxType,0,"fixpt",mxCreateDoubleMatrix(0,0,mxREAL));
      mxSetField(mxData,2,"type",mxType);
    }

    mxSetField(mxData,2,"complexity",mxCreateDoubleScalar(0));

    {
      mxArray *mxSize = mxCreateDoubleMatrix(1,2,mxREAL);
      double *pr = mxGetPr(mxSize);
      pr[0] = (double)(1);
      pr[1] = (double)(1);
      mxSetField(mxData,3,"size",mxSize);
    }

    {
      const char *typeFields[] = { "base", "aliasId", "fixpt" };

      mxArray *mxType = mxCreateStructMatrix(1,1,3,typeFields);
      mxSetField(mxType,0,"base",mxCreateDoubleScalar(10));
      mxSetField(mxType,0,"aliasId",mxCreateDoubleScalar(0));
      mxSetField(mxType,0,"fixpt",mxCreateDoubleMatrix(0,0,mxREAL));
      mxSetField(mxData,3,"type",mxType);
    }

    mxSetField(mxData,3,"complexity",mxCreateDoubleScalar(0));
    mxSetField(mxAutoinheritanceInfo,0,"outputs",mxData);
  }

  return(mxAutoinheritanceInfo);
}

static void chart_debug_initialization(SimStruct *S, unsigned int
  fullDebuggerInitialization)
{
  if (ssIsFirstInitCond(S) && fullDebuggerInitialization==1) {
    /* do this only if simulation is starting */
    if (!sim_mode_is_rtw_gen(S)) {
      {
        unsigned int chartAlreadyPresent;
        chartAlreadyPresent = sf_debug_initialize_chart
          (_conmutadorMachineNumber_,
           2,
           1,
           1,
           6,
           0,
           0,
           0,
           0,
           0,
           &(chartInstance.chartNumber),
           &(chartInstance.instanceNumber),
           ssGetPath(S),
           (void *)S);
        if (chartAlreadyPresent==0) {
          /* this is the first instance */
          init_script_number_translation(_conmutadorMachineNumber_,
            chartInstance.chartNumber);
          sf_debug_set_chart_disable_implicit_casting(_conmutadorMachineNumber_,
            chartInstance.chartNumber,1);
          sf_debug_set_chart_event_thresholds(_conmutadorMachineNumber_,
            chartInstance.chartNumber,
            0,
            0,
            0);
          _SFD_SET_DATA_PROPS(0,1,1,0,SF_DOUBLE,0,NULL,0,0,0,0.0,1.0,0,
                              "destino1",0,c2_sf_marshall);
          _SFD_SET_DATA_PROPS(1,2,0,1,SF_DOUBLE,0,NULL,0,0,0,0.0,1.0,0,
                              "tx_drop1",0,NULL);
          _SFD_SET_DATA_PROPS(2,1,1,0,SF_DOUBLE,0,NULL,0,0,0,0.0,1.0,0,
                              "destino2",0,NULL);
          _SFD_SET_DATA_PROPS(3,2,0,1,SF_DOUBLE,0,NULL,0,0,0,0.0,1.0,0,
                              "tx_drop2",0,NULL);
          _SFD_SET_DATA_PROPS(4,2,0,1,SF_DOUBLE,0,NULL,0,0,0,0.0,1.0,0,
                              "tx_drop3",0,NULL);
          _SFD_SET_DATA_PROPS(5,2,0,1,SF_DOUBLE,0,NULL,0,0,0,0.0,1.0,0,
                              "tx_drop4",0,NULL);
          _SFD_STATE_INFO(0,0,2);
          _SFD_CH_SUBSTATE_COUNT(0);
          _SFD_CH_SUBSTATE_DECOMP(0);
        }

        _SFD_CV_INIT_CHART(0,0,0,0);

        {
          _SFD_CV_INIT_STATE(0,0,0,0,0,0,NULL,NULL);
        }

        _SFD_CV_INIT_TRANS(0,0,NULL,NULL,0,NULL);

        /* Initialization of EML Model Coverage */
        _SFD_CV_INIT_EML(0,3,5,0,4,0,0,0);
        _SFD_CV_INIT_EML_FCN(0,0,"eML_blk_kernel",0,-1,1477);
        _SFD_CV_INIT_EML_FCN(0,1,"asignar_puertos",1477,-1,1830);
        _SFD_CV_INIT_EML_FCN(0,2,"resolver_conflicto",1830,-1,2291);
        _SFD_CV_INIT_EML_IF(0,0,781,802,-1,876);
        _SFD_CV_INIT_EML_IF(0,1,934,965,-1,1099);
        _SFD_CV_INIT_EML_IF(0,2,1548,1561,-1,1666);
        _SFD_CV_INIT_EML_IF(0,3,2143,2160,-1,-2);
        _SFD_CV_INIT_EML_IF(0,4,2198,2216,-1,-2);
        _SFD_CV_INIT_EML_FOR(0,0,329,356,412);
        _SFD_CV_INIT_EML_FOR(0,1,521,553,1103);
        _SFD_CV_INIT_EML_FOR(0,2,740,772,889);
        _SFD_CV_INIT_EML_FOR(0,3,2110,2135,2271);
        _SFD_TRANS_COV_WTS(0,0,0,1,0);
        if (chartAlreadyPresent==0) {
          _SFD_TRANS_COV_MAPS(0,
                              0,NULL,NULL,
                              0,NULL,NULL,
                              1,NULL,NULL,
                              0,NULL,NULL);
        }

        {
          real_T *c2_destino1;
          real_T *c2_tx_drop1;
          real_T *c2_destino2;
          real_T *c2_tx_drop2;
          real_T *c2_tx_drop3;
          real_T *c2_tx_drop4;
          c2_tx_drop1 = (real_T *)ssGetOutputPortSignal(chartInstance.S, 1);
          c2_tx_drop4 = (real_T *)ssGetOutputPortSignal(chartInstance.S, 4);
          c2_destino2 = (real_T *)ssGetInputPortSignal(chartInstance.S, 1);
          c2_tx_drop2 = (real_T *)ssGetOutputPortSignal(chartInstance.S, 2);
          c2_destino1 = (real_T *)ssGetInputPortSignal(chartInstance.S, 0);
          c2_tx_drop3 = (real_T *)ssGetOutputPortSignal(chartInstance.S, 3);
          _SFD_SET_DATA_VALUE_PTR(0U, c2_destino1);
          _SFD_SET_DATA_VALUE_PTR(1U, c2_tx_drop1);
          _SFD_SET_DATA_VALUE_PTR(2U, c2_destino2);
          _SFD_SET_DATA_VALUE_PTR(3U, c2_tx_drop2);
          _SFD_SET_DATA_VALUE_PTR(4U, c2_tx_drop3);
          _SFD_SET_DATA_VALUE_PTR(5U, c2_tx_drop4);
        }
      }
    }
  } else {
    sf_debug_reset_current_state_configuration(_conmutadorMachineNumber_,
      chartInstance.chartNumber,chartInstance.instanceNumber);
  }
}

static void sf_opaque_initialize_c2_conmutador(void *chartInstanceVar)
{
  chart_debug_initialization(chartInstance.S,0);
  initialize_params_c2_conmutador();
  initialize_c2_conmutador();
}

static void sf_opaque_enable_c2_conmutador(void *chartInstanceVar)
{
  enable_c2_conmutador();
}

static void sf_opaque_disable_c2_conmutador(void *chartInstanceVar)
{
  disable_c2_conmutador();
}

static void sf_opaque_gateway_c2_conmutador(void *chartInstanceVar)
{
  sf_c2_conmutador();
}

static void sf_opaque_terminate_c2_conmutador(void *chartInstanceVar)
{
  if (sim_mode_is_rtw_gen(chartInstance.S) || sim_mode_is_external
      (chartInstance.S)) {
    sf_clear_rtw_identifier(chartInstance.S);
  }

  finalize_c2_conmutador();
}

extern unsigned int sf_machine_global_initializer_called(void);
static void mdlProcessParameters_c2_conmutador(SimStruct *S)
{
  int i;
  for (i=0;i<ssGetNumRunTimeParams(S);i++) {
    if (ssGetSFcnParamTunable(S,i)) {
      ssUpdateDlgParamAsRunTimeParam(S,i);
    }
  }

  if (sf_machine_global_initializer_called()) {
    initialize_params_c2_conmutador();
  }
}

static void mdlSetWorkWidths_c2_conmutador(SimStruct *S)
{
  if (sim_mode_is_rtw_gen(S) || sim_mode_is_external(S)) {
    int_T chartIsInlinable =
      (int_T)sf_is_chart_inlinable("conmutador","conmutador",2);
    ssSetStateflowIsInlinable(S,chartIsInlinable);
    ssSetRTWCG(S,sf_rtw_info_uint_prop("conmutador","conmutador",2,"RTWCG"));
    ssSetEnableFcnIsTrivial(S,1);
    ssSetDisableFcnIsTrivial(S,1);
    ssSetNotMultipleInlinable(S,sf_rtw_info_uint_prop("conmutador","conmutador",
      2,"gatewayCannotBeInlinedMultipleTimes"));
    if (chartIsInlinable) {
      ssSetInputPortOptimOpts(S, 0, SS_REUSABLE_AND_LOCAL);
      ssSetInputPortOptimOpts(S, 1, SS_REUSABLE_AND_LOCAL);
      sf_mark_chart_expressionable_inputs(S,"conmutador","conmutador",2,2);
      sf_mark_chart_reusable_outputs(S,"conmutador","conmutador",2,4);
    }

    sf_set_rtw_dwork_info(S,"conmutador","conmutador",2);
    ssSetHasSubFunctions(S,!(chartIsInlinable));
    ssSetOptions(S,ssGetOptions(S)|SS_OPTION_WORKS_WITH_CODE_REUSE);
  }

  ssSetChecksum0(S,(1925629617U));
  ssSetChecksum1(S,(2181669510U));
  ssSetChecksum2(S,(1678781391U));
  ssSetChecksum3(S,(1887000571U));
  ssSetmdlDerivatives(S, NULL);
  ssSetExplicitFCSSCtrl(S,1);
}

static void mdlRTW_c2_conmutador(SimStruct *S)
{
  if (sim_mode_is_rtw_gen(S)) {
    sf_write_symbol_mapping(S, "conmutador", "conmutador",2);
    ssWriteRTWStrParam(S, "StateflowChartType", "Embedded MATLAB");
  }
}

static void mdlStart_c2_conmutador(SimStruct *S)
{
  chartInstance.chartInfo.chartInstance = NULL;
  chartInstance.chartInfo.isEMLChart = 1;
  chartInstance.chartInfo.chartInitialized = 0;
  chartInstance.chartInfo.sFunctionGateway = sf_opaque_gateway_c2_conmutador;
  chartInstance.chartInfo.initializeChart = sf_opaque_initialize_c2_conmutador;
  chartInstance.chartInfo.terminateChart = sf_opaque_terminate_c2_conmutador;
  chartInstance.chartInfo.enableChart = sf_opaque_enable_c2_conmutador;
  chartInstance.chartInfo.disableChart = sf_opaque_disable_c2_conmutador;
  chartInstance.chartInfo.zeroCrossings = NULL;
  chartInstance.chartInfo.outputs = NULL;
  chartInstance.chartInfo.derivatives = NULL;
  chartInstance.chartInfo.mdlRTW = mdlRTW_c2_conmutador;
  chartInstance.chartInfo.mdlStart = mdlStart_c2_conmutador;
  chartInstance.chartInfo.mdlSetWorkWidths = mdlSetWorkWidths_c2_conmutador;
  chartInstance.chartInfo.extModeExec = NULL;
  chartInstance.chartInfo.restoreLastMajorStepConfiguration = NULL;
  chartInstance.chartInfo.restoreBeforeLastMajorStepConfiguration = NULL;
  chartInstance.chartInfo.storeCurrentConfiguration = NULL;
  chartInstance.S = S;
  ssSetUserData(S,(void *)(&(chartInstance.chartInfo)));/* register the chart instance with simstruct */
  if (!sim_mode_is_rtw_gen(S)) {
    init_dsm_address_info();
  }

  chart_debug_initialization(S,1);
}

void c2_conmutador_method_dispatcher(SimStruct *S, int_T method, void *data)
{
  switch (method) {
   case SS_CALL_MDL_START:
    mdlStart_c2_conmutador(S);
    break;

   case SS_CALL_MDL_SET_WORK_WIDTHS:
    mdlSetWorkWidths_c2_conmutador(S);
    break;

   case SS_CALL_MDL_PROCESS_PARAMETERS:
    mdlProcessParameters_c2_conmutador(S);
    break;

   default:
    /* Unhandled method */
    sf_mex_error_message("Stateflow Internal Error:\n"
                         "Error calling c2_conmutador_method_dispatcher.\n"
                         "Can't handle method %d.\n", method);
    break;
  }
}
