package com.example.bommung.common.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter @Setter
public abstract class BaseModel {
    protected String link;

    public void addLink(String link) {
        this.link = link;
    }

    public abstract <E extends BaseEntity> E toEntity();

//    public <T extends BaseApiController, PkType> void addLink(Class<T> tClass, PkType pk) {
//
//        log.debug("# IN BaseModel.addLink : {}", pk);
//
//        this.link = WebMvcLinkBuilder.linkTo(
//                methodOn(tClass).getDetail(pk)).toUri().toString();
//    }

}
