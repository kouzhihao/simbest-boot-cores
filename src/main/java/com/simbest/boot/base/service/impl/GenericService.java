package com.simbest.boot.base.service.impl;

import com.google.common.collect.Sets;
import com.simbest.boot.base.model.GenericModel;
import com.simbest.boot.base.repository.Condition;
import com.simbest.boot.base.repository.GenericRepository;
import com.simbest.boot.base.service.IGenericService;
import com.simbest.boot.constants.ApplicationConstants;
import com.simbest.boot.exceptions.InsertExistObjectException;
import com.simbest.boot.exceptions.UpdateNotExistObjectException;
import com.simbest.boot.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * <strong>Title : 基础实体通用服务层</strong><br>
 * <strong>Description : 基础实体通用服务层</strong><br>
 * <strong>Create on : 14:52</strong><br>
 * <strong>Modify on : 14:52</strong><br>
 * <strong>Copyright (C) Ltd.</strong><br>
 *
 * @author LJW lijianwu@simbest.com.cn
 * @version <strong>V1.0.0</strong><br>
 * <strong>修改历史:</strong><br>
 * 修改人 修改日期 修改描述<br>
 * 增加缓存
 * -------------------------------------------<br>
 */
@Slf4j
@CacheConfig(cacheNames = ApplicationConstants.REDIS_DEFAULT_CACHE_PREFIX)
public class GenericService<T extends GenericModel,PK extends Serializable> implements IGenericService<T,PK> {

    private GenericRepository<T,PK> genericRepository;

    public GenericService(){}

    public GenericService ( GenericRepository<T, PK> genericRepository ) {
        this.genericRepository = genericRepository;
    }

    @Override
    public Pageable getPageable(int page, int size, String direction, String properties) {
        return genericRepository.getPageable(page,size,direction,properties);
    }

    @Override
    public Specification<T> getSpecification(Condition conditions) {
        return genericRepository.getSpecification(conditions);
    }

    /**
     * @see
     */
    @Override
    public long count ( ) {
        log.debug("@Generic Repository Service count null param");
        return genericRepository.count();
    }

    /**
     * @see
     */
    @Override
    public long count ( Specification<T> specification ) {
        log.debug("@Generic Repository Service count Specification param");
        return genericRepository.count( specification );
    }

    /**
     * @see
     */
    @Override
    public boolean exists ( PK id ) {
        log.debug("@Generic Repository Service exists object by id: " + id);
        return genericRepository.existsById( id );
    }

    /**
     * @see
     */
    @Override
    public T getOne ( PK id ) {
        log.debug("@Generic Repository Service getOne object by id: " + id);
        return genericRepository.getOne(id);
    }

    /**
     * @see
     */
    @Override
    public T findById ( PK id ) {
        log.debug("@Generic Repository Service get single object by id: " + id);
        return genericRepository.findById(id).orElse( null );
    }

    /**
     * @see
     * @return
     */
    @Override
    public Page<T> findAll ( ) {
        log.debug("@Generic Repository Service getAll");
        return genericRepository.findAll(genericRepository.getPageable());
    }

    /**
     * @see
     */
    @Override
    public Page<T>  findAll ( Pageable pageable ) {
        log.debug("@Generic Repository Service findAll object PageSize:" + pageable.getPageSize() + ":PageNumber:" + pageable.getPageNumber());
        return genericRepository.findAll( pageable );
    }

    /**
     * @see
     * @param sort  排序字段
     * @return
     */
    @Override
    public Page<T>  findAll ( Sort sort ) {
        log.debug("@Generic Repository Service object by Sort");
        return genericRepository.findAll(PageRequest.of(ApplicationConstants.DEFAULT_PAGE, ApplicationConstants.DEFAULT_SIZE, sort));
    }

    @Override
    public List<T> findAllByIDs(Iterable<PK> ids) {
        log.debug("@Generic Repository Service object by findAllByIDs");
        return genericRepository.findAllById(ids);
    }

    /**
     * @see
     */
    @Override
    public Page<T> findAll ( Specification<T> conditions, Pageable pageable ) {
        log.debug("@Generic Repository Service findAll Specification object PageSize:" + pageable.getPageSize() + ":PageNumber:" + pageable.getPageNumber());
        return genericRepository.findAll(conditions, pageable);
    }

    /**
     * @see
     */
    @Override
    @Transactional
    public T insert ( T o ) {
        if(null == ObjectUtil.getEntityIdVaue(o)) {
            log.debug("@Generic Repository Service create new object: " + o);
            return genericRepository.save(o);
        } else {
            throw new InsertExistObjectException();
        }
    }

    @Override
    @Transactional
    public T update ( T o ) {
        if(null != ObjectUtil.getEntityIdVaue(o)) {
            log.debug("@Generic Repository Service update a already object: " + o);
            return genericRepository.save(o);
        } else {
            throw new UpdateNotExistObjectException();
        }
    }

    /**
     * @see
     */
    @Override
    @Transactional
    public <S extends T> Iterable<S> saveAll ( Iterable<? extends T> param ) {
        log.debug("@Generic Repository Service saveAll");
        return (Iterable<S>)genericRepository.saveAll( param );
    }


    /**
     * @see
     */
    @Override
    @Transactional
    public T saveAndFlush ( T o ) {
        log.debug("@Generic Repository Service saveAndFlush object:" + o);
        return genericRepository.saveAndFlush( o );
    }

    /**
     * @see
     */
    @Override
    @Transactional
    public void deleteById ( PK id ) {
        log.debug("@Generic Repository Service deleteById object by id: " + id);
        genericRepository.deleteById( id );
    }

    /**
     * @see
     */
    @Override
    @Transactional
    public void delete ( T o ) {
        log.debug("@Generic Repository Service delete object: " + o);
        genericRepository.delete( o );
    }

    /**
     * @see
     */
    @Override
    @Transactional
    public void deleteAll ( Iterable<? extends T> iterable ) {
        log.debug("@Generic Repository Service deleteAll Iterable param");
        genericRepository.deleteAll( iterable );
    }

    /**
     * @see
     */
    @Override
    @Transactional
    public void deleteAll ( ) {
        log.debug("@Generic Repository Service deleteAll null param");
        genericRepository.deleteAll();
    }

    /**
     * @see
     */
    @Override
    @Transactional
    public void deleteAllByIds ( Iterable<? extends PK> ids ) {
        log.debug("@Generic Repository Service deleteAllByIds Iterable param");
        Set<T> psSet = Sets.newHashSet();
        for(PK pk: ids){
            psSet.add(genericRepository.findById(pk).orElse(null));
        }
        genericRepository.deleteAll( psSet );
    }


}