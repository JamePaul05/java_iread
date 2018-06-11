package com.ifuture.iread.service.impl;

import com.ifuture.iread.entity.Book;
import com.ifuture.iread.entity.Booklet;
import com.ifuture.iread.entity.User;
import com.ifuture.iread.repository.BookRepository;
import com.ifuture.iread.repository.UserRepository;
import com.ifuture.iread.service.IBookService;
import com.ifuture.iread.service.IBookletService;
import com.ifuture.iread.util.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.OrderComparator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.parser.OrderBySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.OrderBy;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.math.BigInteger;
import java.util.*;

/**
 * Created by maofn on 2017/3/26.
 */
@Service
public class BookServiceImpl implements IBookService {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IBookletService bookletService;

    @Override
    public DataTableReturnObject fetchBooks(DataRequest dr) {
        DataTableReturnObject dro = new DataTableReturnObject();
        try {
            dro.setDraw(Integer.valueOf(dr.getDraw()));
        } catch (Exception e) {
            if (e instanceof NumberFormatException) {
                throw new RuntimeException("datatables传的draw参数有误！");
            }
        }
        Pageable pageable;
        String orderField = dr.getOrderField();
        String direction = dr.getDirection();
        final String search = dr.getSearch();
        int pageIndex = dr.getPage();
        int rows = dr.getRows();
        if ("bookName".equals(orderField)) {
            orderField = "pinyin";
        }
        Sort.Direction dir = DataTablesContants.DIRECTION_ASC.equalsIgnoreCase(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort.Order order = new Sort.Order(dir, orderField, Sort.NullHandling.NULLS_LAST);
        Sort sort = new Sort(order);
        pageable = new PageRequest(pageIndex - 1, rows, sort);

        //如果是书册数量
        /*if ("bookletCount".equals(orderField)) {
            pageable = new PageRequest(pageIndex - 1, rows);

        } else {
            //排序方向
            Sort.Direction dir = DataTablesContants.DIRECTION_ASC.equalsIgnoreCase(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;
            Sort.Order order;
            if ("bookName".equals(orderField)) {
                order = new Sort.Order(dir, "pinyin", Sort.NullHandling.NULLS_LAST);
            } else {
                //空字段排最后
                order = new Sort.Order(dir, orderField, Sort.NullHandling.NULLS_LAST);
            }
            Sort sort = new Sort(order);
            pageable = new PageRequest(pageIndex - 1, rows, sort);
        }*/
        Page<Book> page = bookRepository.findAll(new Specification<Book>() {
            @Override
            public Predicate toPredicate(Root<Book> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.and();
                if (StringUtil.isNotNull(search)){
                    Predicate like2 = cb.like(root.get("isbn").as(String.class), "%" + search + "%");
                    Predicate like1 = cb.like(root.get("bookName").as(String.class), "%" + search + "%");
                    Predicate like3 = cb.like(root.get("author").as(String.class), "%" + search + "%");
                    Predicate like4 = cb.like(root.get("press").as(String.class), "%" + search + "%");
                    predicate = cb.or(like1, like2, like3, like4);
                }
                query.where(predicate);
               /* if ("bookletCount".equals(orderField)) {
                    SetJoin<Book, Booklet> join = root.join(root.getModel().getSet("bookletSet", Booklet.class), JoinType.RIGHT);
                    Predicate groupBy = query.groupBy(join).getRestriction();
                    Order order = null;
                    if("desc".equalsIgnoreCase(direction)) {
                        order = cb.desc(cb.count(groupBy));
                    } else {
                        order = cb.asc(cb.count(groupBy));
                    }
                    query.orderBy(order);
                }*/
                return query.getRestriction();
            }
        }, pageable);

        List<JSONObject> list=new ArrayList<>();
        for(Book book : page.getContent()) {
            JSONObject j = new JSONObject();
            j.put("id", book.getId());
            j.put("isbn", book.getIsbn());
            j.put("bookName", book.getBookName());
            j.put("author", book.getAuthor());
            j.put("press", book.getPress());
            j.put("bookletsTotal", book.getBookletsTotal());
            list.add(j);
        }
        dro.setRecordsTotal(bookRepository.findAll().size());
        dro.setRecordsFiltered(page.getTotalElements());
        dro.setData(list);
        return dro;
    }

    @Override
    @Transactional
    public boolean remove(String ids) {
        try {
            if (StringUtil.isNotNull(ids)) {
                /*if(ids.contains(",")){
                    String[] idList = ids.split(",");
                    for(int i = 0; i < idList.length; i++){
                        bookRepository.delete(idList[i]);
                    }
                }else{
                    bookRepository.delete(ids);
                }*/
                if (StringUtil.isNotNull(ids)) {
                    // 删除书册
                    String[] idList = ids.split(",");
                    StringBuilder sb = new StringBuilder("delete from booklet where book_id in (");
                    for (String id : idList) {
                        sb.append("'");
                        sb.append(id);
                        sb.append("'");
                        sb.append(",");
                    }
                    if (ids != null && idList.length > 0) {
                        sb.deleteCharAt(sb.length() - 1);
                    }
                    sb.append(")");
                    Query query = em.createNativeQuery(sb.toString(), Booklet.class);
                    query.executeUpdate();

                    // 删除书本
                    StringBuilder sb2 = new StringBuilder("delete from book where id in (");
                    for (String id : idList) {
                        sb2.append("'");
                        sb2.append(id);
                        sb2.append("'");
                        sb2.append(",");
                    }
                    if (ids != null && idList.length > 0) {
                        sb2.deleteCharAt(sb2.length() - 1);
                    }
                    sb2.append(")");
                    Query query2 = em.createNativeQuery(sb2.toString(), Book.class);
                    query2.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Book findOneById(String id) {
        return bookRepository.findOne(id);
    }

    @Override
    public boolean create(Book book) {
        if (bookRepository.findByIsbn(book.getIsbn()) != null) {
            return false;
        }
        book.setPinyin(PinyinUtil.hanyuConvertToPinyin(book.getBookName()));
        try {
            User createrOrUpdater = SecurityUtil.getUser(userRepository);
            book.setId(UUIDUtil.generateUUID());
            book.setCreatedBy(createrOrUpdater);
            book.setCreateDate(new Date());
            book.setUpdateDate(new Date());
            book.setUpdatedBy(createrOrUpdater);
            book.setBookletsTotal(0);
            bookRepository.save(book);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean exist(String isbn) {
        return bookRepository.findByIsbn(isbn) != null;
    }

    @Override
    public void update(Book book) {
        book.setPinyin(PinyinUtil.hanyuConvertToPinyin(book.getBookName()));
        User createrOrUpdater = SecurityUtil.getUser(userRepository);
        Book old = bookRepository.findOne(book.getId());
        book.setCreatedBy(old.getCreatedBy());
        book.setCreateDate(old.getCreateDate());
        book.setUpdateDate(new Date());
        book.setUpdatedBy(createrOrUpdater);
        bookRepository.save(book);
    }

    @Override
    public List<Book> listBooksByShopID(String shopID, String bookName) {
        List<Booklet> booklets = bookletService.listBookletsByShopID(shopID);
        List<Book> books = new ArrayList<>();
        Map<String, Book> map = new HashMap<String, Book>();
        //循环书册集，如果books里不存在书册对应的书本，就添加到books
        for (Booklet booklet : booklets) {
            Book book = booklet.getBook();
            if (book != null && !map.containsKey(book.getId())) {
                if (StringUtils.isEmpty(bookName)) {
                    map.put(booklet.getBook().getId(), booklet.getBook());
                    books.add(book);
                }
                if (!StringUtils.isEmpty(bookName) && book.getBookName().contains(bookName)) {
                    map.put(booklet.getBook().getId(), booklet.getBook());
                    books.add(book);
                }
            }
        }
        return books;
    }

    /**
     * 根据借阅次数排序
     * @param books
     * @param shopId
     * @return
     */
    @Override
    public List<Book> sortByBorrowTimes(List<Book> books, String shopId) {
        Collections.sort(books, new Comparator<Book>() {
            @Override
            public int compare(Book o1, Book o2) {
                return o2.getBorrowTimes() - o1.getBorrowTimes();
            }
        });
        // 可借书册数量
        for (Book book: books) {
            BigInteger count = getBookletCount(book.getId(), shopId);
            book.setAllowBookletBorrowNumber(count.intValue());
        }
        return books;
    }

    @Override
    public Book findOneByIsbn(String isbn) {
        return bookRepository.findOneByIsbn(isbn);
    }

    /**
     * 判断书册对应的书本在书本集合中是否存在
     * @param books
     * @param booklet
     * @return
     */
    private boolean hasBook(List<Book> books, Booklet booklet, String bookName) {
        String isbn = booklet.getBook().getIsbn();
        for (Book book : books) {
            if (book.getIsbn().equalsIgnoreCase(isbn) && book.getBookName() != null && book.getBookName().contains(bookName)) {
                return true;
            }
        }
        return false;
    }

    private BigInteger getBookletCount(String bookId, String shopId) {
        StringBuilder sb = new StringBuilder("select count(*) from booklet where book_id = '");
        sb.append(bookId);
        sb.append("'");
        if (!StringUtils.isEmpty(shopId)) {
            sb.append(" and shop_id = '");
            sb.append(shopId);
            sb.append("'");
            sb.append(" and borrowed = 0");
        }
        sb.append(" group by book_id ");

        Query query = em.createNativeQuery(sb.toString());
        List result = query.getResultList();
        if (result != null && result.size() > 0) {
            return (BigInteger) result.get(0);
        } else {
            return BigInteger.ZERO;
        }
    }
}
