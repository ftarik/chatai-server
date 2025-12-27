//package fr.fgroup.chatai.converters;
//
//import fr.fgroup.chatai.resources.bcp.get.RequestDetailResource;
//import fr.fgroup.chatai.resources.bcp.get.RequestListItemResource;
//import fr.fgroup.chatai.resources.bcp.get.RequestResourceGet;
//import fr.fgroup.chatai.resources.bcp.post.RequestResourceCreate;
//import fr.fgroup.chatai.resources.bcp.put.RequestResourceUpdate;
//import fr.fgroup.chatai.resources.bcp.put.RequestUpdateByDirectorResource;
//import org.mapstruct.*;
//
//@Mapper(
//        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
//        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//public interface RequestMapper {
//  RequestListItemResource requestEntityToRequestListItemResource(RequestEntity requestEntity);
//
//  RequestEntity requestResourceToRequest(RequestResourceCreate requestResourceCreate);
//
//  RequestEntity requestResourceToRequest(RequestResourceUpdate requestResourceUpdate,
//                                         @MappingTarget RequestEntity requestEntity);
//
//  RequestEntity requestResourceToRequest(RequestUpdateByDirectorResource requestResource,
//                                         @MappingTarget RequestEntity requestEntity);
//
//  RequestResourceGet requestEntityToRequestResource(RequestEntity requestEntity);
//
//  RequestDetailResource requestEntityToRequestDetailResource(RequestEntity requestEntity);
//}
