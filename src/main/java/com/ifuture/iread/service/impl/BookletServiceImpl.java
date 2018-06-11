package com.ifuture.iread.service.impl;

import com.ifuture.iread.entity.*;
import com.ifuture.iread.repository.*;
import com.ifuture.iread.service.IBookletService;
import com.ifuture.iread.util.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by maofn on 2017/3/27.
 */
@Service
public class BookletServiceImpl implements IBookletService {

    @Autowired
    private BookletRepository bookletRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BorrowRecordRepository borrowRecordRepository;

    @Autowired
    private BookletTraceRepository bookletTraceRepository;

    /**
     * @param isbn
     * @param start
     * @param end
     */
    @Override
    public boolean save(String isbn, String start, String end) {
        int first = Integer.valueOf(start);
        int last = Integer.valueOf(end);
        List<String> bianHaos = new ArrayList<>();

        for(;first <= last; first++) {
            String bianHao = "";
            if(first < 10) {
                bianHao = isbn + "-" + "0" + first;
            } else {
                bianHao = isbn + "-" + first;
            }
            //如果数据库中有一条编号是已经存在的，就返回false
            if (bookletRepository.findOneByIndex(bianHao) != null) {
                return false;
            }
            bianHaos.add(bianHao);
        }
        User createrOrUpdater = SecurityUtil.getUser(userRepository);
        Book book = bookRepository.findByIsbn(isbn);
        for (String bianHao : bianHaos) {
            Booklet booklet = new Booklet();
            booklet.setId(UUIDUtil.generateUUID());
            booklet.setCreateDate(new Date());
            booklet.setUpdateDate(new Date());
            booklet.setCreatedBy(createrOrUpdater);
            booklet.setUpdatedBy(createrOrUpdater);
            booklet.setBianHao(bianHao);
            booklet.setBook(book);
            booklet.setState(Constants.LU_RU_XI_TONG);
            bookletRepository.save(booklet);
        }
        //更新书本中书册总数字段
        Book book1 = bookRepository.findByIsbn(isbn);
        book1.setBookletsTotal(getBookletsTotal(book1.getId()));
        bookRepository.save(book1);
        return true;
    }

    private int getBookletsTotal(String bookId) {
        return bookletRepository.countBookletByBookId(bookId);
    }

    @Override
    public DataTableReturnObject fetchBooklets(DataRequest dr, HttpServletRequest request) {
        DataTableReturnObject dro = new DataTableReturnObject();
        try {
            dro.setDraw(Integer.valueOf(dr.getDraw()));
        } catch (Exception e) {
            if (e instanceof NumberFormatException) {
                throw new RuntimeException("datatables传的draw参数有误！");
            }
        }
        String orderField = dr.getOrderField();
        String direction = dr.getDirection();
        int pageIndex = dr.getPage();
        int rows = dr.getRows();
        //如果是书名排序
        if ("bookName".equals(orderField)) {
            orderField = "book.pinyin";
        }
        //如果是公益点名称排序
        if ("shopName".equals(orderField)) {
            orderField = "shop.pinyin";
        }
        //排序方向
        Sort.Direction dir = DataTablesContants.DIRECTION_ASC.equalsIgnoreCase(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;
        //空字段排最后
        Sort.Order order = new Sort.Order(dir, orderField, Sort.NullHandling.NULLS_LAST);
        Sort sort = new Sort(order);
        Pageable pageable = new PageRequest(pageIndex - 1, rows, sort);
        Page<Booklet> page = null;
        final String search = dr.getSearch();
        final String shopNameSearch = DataTableUtil.handleCN(request.getParameter(DataTablesContants.ORDER_FIELD_PREFIX + 3 + DataTablesContants.COLUMN_SEARCH_VALUE));
        final String stateSearch = DataTableUtil.handleCN(request.getParameter(DataTablesContants.ORDER_FIELD_PREFIX + 4 + DataTablesContants.COLUMN_SEARCH_VALUE));
        if (StringUtil.isNotNull(search) || StringUtil.isNotNull(shopNameSearch) || StringUtil.isNotNull(stateSearch)){
            page = bookletRepository.findAll(new Specification<Booklet>() {
                @Override
                public Predicate toPredicate(Root<Booklet> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    Predicate predicate = cb.and();
                    //如果 搜索框搜索不是空
                    if (StringUtil.isNotNull(search)) {
                        Predicate like2 = cb.like(root.get("bianHao").as(String.class), "%" + search + "%");
                        Predicate like3 = cb.like(root.get("book").get("bookName").as(String.class), "%" + search + "%");
                        Predicate like5 = cb.like(root.join("borrower", JoinType.LEFT).get("memberName").as(String.class), "%" + search + "%");
                        predicate = cb.and(predicate, cb.or(like2, like3, like5));
                    }
                    //如果公益点名称不是空
                    if (StringUtil.isNotNull(shopNameSearch)) {
                        Predicate like1 = cb.like(root.join("shop", JoinType.LEFT).get("shopName").as(String.class), "%" + shopNameSearch + "%");
                        predicate = cb.and(predicate, like1);
                    }
                    //如果状态搜搜此不是空
                    if (StringUtil.isNotNull(stateSearch)) {
                        Predicate like4 = cb.like(root.get("state").as(String.class), "%" + stateSearch + "%");
                        predicate = cb.and(predicate, like4);
                    }
                    return predicate;
                }
            }, pageable);
        } else {
            page = bookletRepository.findAll(pageable);
        }

        List<JSONObject> list=new ArrayList<>();
        for(Booklet booklet : page.getContent()) {
            JSONObject j = new JSONObject();
            j.put("id", booklet.getId());
            j.put("bianHao", booklet.getBianHao());
            j.put("bookName", booklet.getBook().getBookName());
            j.put("shopName", booklet.getShop() == null ? "空" : booklet.getShop().getShopName());
            j.put("state", booklet.getState());
            j.put("borrowed", booklet.isBorrowed() ? "是" : "否");
            j.put("borrowerName", booklet.getBorrower() == null ? "空" : booklet.getBorrower().getMemberName());
            list.add(j);
        }
        dro.setRecordsTotal(bookletRepository.findAll().size());
        dro.setRecordsFiltered(page.getTotalElements());
        dro.setData(list);
        return dro;
    }

    @Override
    public boolean remove(String ids) {
        try {
            if (StringUtil.isNotNull(ids)) {
                if(ids.contains(",")){
                    String[] idList = ids.split(",");
                    for(int i = 0; i < idList.length; i++){
                        //先找出所有的借书记录
                        List<BorrowRecord> brs = borrowRecordRepository.findAllByBookletId(idList[i]);
                        boolean allReturn = true;
                        //循环，如果有未归还的，就删除失败
                        for (BorrowRecord br : brs) {
                            if (!StringUtil.isNotNull(br.getReturnId())) {
                                allReturn = false;
                                break;
                            }
                        }
                        if (allReturn) {
                            Booklet booklet = bookletRepository.findOne(idList[i]);
                            bookletRepository.delete(booklet);
                            borrowRecordRepository.deleteByBookletId(idList[i]);
                            //更新书本中书册总数字段
                            Book book1 = bookRepository.findOne(booklet.getBook().getId());
                            book1.setBookletsTotal(getBookletsTotal(book1.getId()));
                            bookRepository.save(book1);
                        } else {
                            return false;
                        }
                    }
                } else {
                    List<BorrowRecord> brs = borrowRecordRepository.findAllByBookletId(ids);
                    boolean allReturn = true;
                    for (BorrowRecord br : brs) {
                        if (!StringUtil.isNotNull(br.getReturnId())) {
                            allReturn = false;
                            break;
                        }
                    }
                    if (allReturn) {
                        Booklet booklet = bookletRepository.findOne(ids);
                        bookletRepository.delete(booklet);
                        borrowRecordRepository.deleteByBookletId(ids);
                        //更新书本中书册总数字段
                        Book book1 = bookRepository.findOne(booklet.getBook().getId());
                        book1.setBookletsTotal(getBookletsTotal(book1.getId()));
                        bookRepository.save(book1);
                    } else {
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 调拨功能，可以将书册批量的转移到另一个公益点
     * @param ids       书册id字符串。形如:id, id, id
     * @param shopID    公益点id
     * @return
     */
    @Override
    public Boolean allocate(String ids, String shopID) {
        try {
            Shop shop = shopRepository.findOne(shopID);
            if(ids.contains(",")){
                String[] idList = ids.split(",");
                for(int i = 0; i < idList.length; i++){
                    Booklet temp = bookletRepository.findOne(idList[i]);
                    //只有录入系统状态或者馆藏状态的书册能调拨
                    if (temp.getState().equals(Constants.LU_RU_XI_TONG) || temp.getState().equals(Constants.GUAN_CANG)) {
                        //先保存书本迹点表
                        BookletTrace bookletTrace = new BookletTrace();
                        bookletTrace.setId(UUIDUtil.generateUUID());
                        bookletTrace.setBookletId(temp.getId());
                        bookletTrace.setOutId(temp.getState().equals(Constants.GUAN_CANG) ? temp.getShop().getId() : null);
                        bookletTrace.setEntryId(shop.getId());
                        bookletTrace.setAllocateDate(new Date());
                        bookletTraceRepository.save(bookletTrace);
                        //在更新书册信息
                        temp.setShop(shop);
                        temp.setState(Constants.GUAN_CANG);
                        this.update(temp);

                    }


                }
            } else {
                Booklet temp = bookletRepository.findOne(ids);
                //先保存书本迹点表
                BookletTrace bookletTrace = new BookletTrace();
                bookletTrace.setId(UUIDUtil.generateUUID());
                bookletTrace.setBookletId(temp.getId());
                bookletTrace.setOutId(temp.getState().equals(Constants.GUAN_CANG) ? temp.getShop().getId() : null);
                bookletTrace.setEntryId(shop.getId());
                bookletTrace.setAllocateDate(new Date());
                bookletTraceRepository.save(bookletTrace);
                temp.setShop(shop);
                temp.setState(Constants.GUAN_CANG);
                this.update(temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public List<Booklet> listBookletsByShopID(String shopID) {
        return bookletRepository.findByShopID(shopID);
    }

    /**
     * 借书接口
     * 开启事务，保存失败回滚操作
     * @param bianhao
     * @param borrower
     * @return
     */
    @Transactional
    @Override
    public int borrowBook(String bianhao, Member borrower) {
        Booklet booklet = bookletRepository.findOneByIndex(bianhao);
        //如果书册为空或者会员为空
        if (booklet == null) {
            return 1;
        }
        //如果书被借出
        if (booklet.getBorrower() != null && booklet.isBorrowed()) {
            return 2;
        }
        //如果会员已经借的数量大于等于了对应会员权益的数量
        if (borrower != null && borrower.getBooklets() != null && borrower.getBooklets().size() > 0) {
            int maxBorrowNum = borrower.getMemberLevel().getMaxBorrowNum();
            if (borrower.getBooklets().size() >= maxBorrowNum) {
                return 3;
            }
        }

        try {
            booklet.setState(Constants.JIE_CHU);
            booklet.setBorrowed(true);
            booklet.setBorrower(borrower);
            booklet.setBorrowDate(new Date());
            List<Booklet> booklets = borrower.getBooklets() == null ? new ArrayList<Booklet>() : borrower.getBooklets();
            booklets.add(booklet);
            borrower.setBooklets(booklets);
            Book book = booklet.getBook();
            int borrowTimes = book.getBorrowTimes();
            book.setBorrowTimes(++borrowTimes);
            bookRepository.save(book);
            bookletRepository.save(booklet);
            memberRepository.save(borrower);
            BorrowRecord br = new BorrowRecord();
            br.setId(UUIDUtil.generateUUID());
            br.setBookletId(booklet.getId());
            br.setBorrowerId(borrower.getId());
            br.setBorrowShopId(booklet.getShop().getId());
            br.setBorrowTime(new Date());
            borrowRecordRepository.save(br);
        } catch (Exception e) {
            e.printStackTrace();
            return 500;
        }
        return 200;
    }

    /**
     * 还书接口
     * 开启事务，保存失败回滚操作
     * @param bianhao
     * @param returner
     * @return
     */
    @Transactional
    @Override
    public int returnBook(String bianhao, User returner) {
        Booklet booklet = bookletRepository.findOneByIndex(bianhao);
        Member borrower = booklet.getBorrower();
        //如果书已经归还
        if (borrower == null || !booklet.isBorrowed()) {
            return 2;
        }
        try {
            //如果是在其他公益点还的书，需要增加书本迹点表
            if (!booklet.getShop().getId().equals(returner.getShop().getId())) {
                //先保存书本迹点表
                BookletTrace bookletTrace = new BookletTrace();
                bookletTrace.setId(UUIDUtil.generateUUID());
                bookletTrace.setBookletId(booklet.getId());
                bookletTrace.setEntryId(returner.getShop().getId());
                bookletTrace.setOutId(booklet.getShop().getId());
                bookletTrace.setAllocateDate(new Date());
                bookletTraceRepository.save(bookletTrace);
            }
            //修改书册的借阅信息
            booklet.setState(Constants.GUAN_CANG);
            booklet.setBorrowed(false);
            booklet.setBorrower(null);
            booklet.setReturner(returner);
            booklet.setBorrowDate(null);
            booklet.setReturnDate(new Date());
            booklet.setShop(returner.getShop());
            bookletRepository.save(booklet);
            //修改会员的结束情况
            List<Booklet> booklets = borrower.getBooklets();
            Iterator<Booklet> it = booklets.iterator();
            while (it.hasNext()) {
                Booklet booklet1 = it.next();
                if (booklet1.getBianHao().equals(bianhao)) {
                    it.remove();
                }
            }
            borrower.setBooklets(booklets);
            memberRepository.save(borrower);

            //修改借书记录
            BorrowRecord borrowRecord = borrowRecordRepository.findOneByBookletIdAndBorrowerId(booklet.getId(), borrower.getId());
            borrowRecord.setReturnId(returner.getId());
            borrowRecord.setReturnShopId(returner.getShop().getId());
            borrowRecord.setReturnTime(new Date());
            borrowRecordRepository.save(borrowRecord);

        } catch (Exception e) {
            e.printStackTrace();
            return 500;
        }
        return 200;
    }

    @Override
    public Booklet findOneById(String bookletId) {
        return bookletRepository.findOne(bookletId);
    }

    @Override
    public Booklet findOneByIndex(String bookletId) {
        return bookletRepository.findOneByIndex(bookletId);
    }

    @Override
    public int countByShop(Shop shop) {
        return bookletRepository.countByShop(shop.getId());
    }

    @Override
    public int countBorrowedByShop(Shop shop) {
        return bookletRepository.countBorrowedByShop(shop.getId());
    }

    private void update(Booklet booklet) {
        User updater = SecurityUtil.getUser(userRepository);
        booklet.setUpdatedBy(updater);
        booklet.setUpdateDate(new Date());
        bookletRepository.save(booklet);
    }
}
