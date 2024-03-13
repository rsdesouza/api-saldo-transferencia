package br.com.bankdesafio.apisaldotransferencia.mapper;

import br.com.bankdesafio.apisaldotransferencia.dto.TransferenciaDTO;
import br.com.bankdesafio.apisaldotransferencia.model.Transacao;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public interface TransferenciaMapper extends BaseMapper<TransferenciaDTO, Transacao> {
}