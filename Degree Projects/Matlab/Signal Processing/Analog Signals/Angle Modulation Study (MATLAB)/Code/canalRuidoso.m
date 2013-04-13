%% CANAL RUIDOSO

function out = canalRuidoso(in,nivel)
% ENTRADA:
% in -> señal que llega al canal
% nivel -> SNR
%
% SALIDA:
% out -> señal que sale del canal

out = awgn(in,nivel); 