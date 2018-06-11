package com.ifuture.iread.controller;

import com.ifuture.iread.entity.Member;
import com.ifuture.iread.entity.SessionRecord;
import com.ifuture.iread.repository.MemberRepository;
import com.ifuture.iread.repository.SessionRecordRepository;
import com.ifuture.iread.service.IMemberService;
import com.ifuture.iread.service.ISessionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by maofn on 2017/2/23.
 */
@Controller
@RequestMapping("/test")
public class TestController {

    @Autowired
    private IMemberService memberService;

    @Autowired
    private ISessionRecordService sessionRecordService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SessionRecordRepository sessionRecordRepository;

    @RequestMapping("delete")
    public void delete() {

        /*Member member = memberService.findOneById("5b30334a-bdd9-4478-ae9c-fc2c5d462ef7");

        SessionRecord s1 = new SessionRecord(member.getWechatId(), member);

        sessionRecordService.save(s1);*/

//        Member member = memberService.findOneById("5b30334a-bdd9-4478-ae9c-fc2c5d462ef7");
//        memberService.remove("5b30334a-bdd9-4478-ae9c-fc2c5d462ef7");

        /*Member member = new Member();
        member.setIdNumber("1");
        member.setEmail("11");
        member.setMemberName("11");
        member.setMobile("11");
        member.setAddress("11");
        member.setId("11");
        SessionRecord s = new SessionRecord();
        s.setOpenId("1");
        member.setSessionRecord(s);
        s.setMember(member);
        memberRepository.save(member);*/
//        memberRepository.delete(member);
        sessionRecordRepository.delete("1");
    }

    public static void main(String[] args) {
        DecimalFormat df = new DecimalFormat("#.00");

        System.out.println("---------AB1B2B3----------");
        double[][] AB1B2B3 = {{1.0, 1.0/9.0, 2.0}, {1.0, 9.0}, {1.0}};
        double[][] arr1 = getArr(AB1B2B3, df);
        System.out.println("wi");
        BigDecimal[] wi1 = getWi(arr1);
        System.out.println("wi0");
        BigDecimal[] wi01 = getWi0(wi1);
        System.out.println("rmi");
        BigDecimal[] rmi1 = getrmi(wi01, arr1);
        System.out.println("rmax");
        BigDecimal rmax1 = getrmax(rmi1);
        System.out.println("ci");
        BigDecimal ci1 = getci(rmax1, rmi1.length);
        System.out.println();

        System.out.println("---------B1C1C2C3----------");
        double[][] B1C1C2C3 = {{1.0, 1.0/3.0, 1.0/9.0}, {1.0, 1.0/7.0}, {1.0}};
        double[][] arr2 = getArr(B1C1C2C3, df);
        System.out.println("wi");
        BigDecimal[] wi2 = getWi(arr2);
        System.out.println("wi0");
        BigDecimal[] wi02 = getWi0(wi2);
        System.out.println("rmi");
        BigDecimal[] rmi2 = getrmi(wi02, arr2);
        System.out.println("rmax");
        BigDecimal rmax2 = getrmax(rmi2);
        System.out.println("ci");
        BigDecimal ci2 = getci(rmax2, rmi2.length);
        System.out.println();

        System.out.println("---------B2C4C5C6----------");
        double[][] B2C4C5C6 = {{1.0, 1.0/2.0, 1.0/3.0}, {1.0, 2.0}, {1.0}};
        double[][] arr3 = getArr(B2C4C5C6, df);
        System.out.println("wi");
        System.out.println();
        BigDecimal[] wi3 = getWi(arr3);
        System.out.println("wi0");
        System.out.println();
        BigDecimal[] wi03 = getWi0(wi3);
        System.out.println("rmi");
        BigDecimal[] rmi3 = getrmi(wi03, arr3);
        System.out.println("rmax");
        BigDecimal rmax3 = getrmax(rmi3);
        System.out.println("ci");
        BigDecimal ci3 = getci(rmax3, rmi3.length);
        System.out.println();

        System.out.println("---------B3C7C8C9----------");
        double[][] B3C7C8C9 = {{1.0, 2.0, 2.0}, {1.0, 1.0/7.0}, {1.0}};
        double[][] arr4 = getArr(B3C7C8C9, df);
        System.out.println("wi");
        System.out.println();
        BigDecimal[] wi4 = getWi(arr4);
        System.out.println("wi0");
        System.out.println();
        BigDecimal[] wi04 = getWi0(wi4);
        System.out.println("rmi");
        BigDecimal[] rmi4 = getrmi(wi04, arr4);
        System.out.println("rmax");
        BigDecimal rmax4 = getrmax(rmi4);
        System.out.println("ci");
        BigDecimal ci4 = getci(rmax4, rmi4.length);
        System.out.println();

        System.out.println("---------C1D1D2D3----------");
        double[][] C1D1D2D3 = {{1.0, 2.0, 1.0/9.0}, {1.0, 1.0/9.0}, {1.0}};
        double[][] arr5 = getArr(C1D1D2D3, df);
        System.out.println("wi");
        System.out.println();
        BigDecimal[] wi5 = getWi(arr5);
        System.out.println("wi0");
        System.out.println();
        BigDecimal[] wi05 = getWi0(wi5);
        System.out.println("rmi");
        BigDecimal[] rmi5 = getrmi(wi05, arr5);
        System.out.println("rmax");
        BigDecimal rmax5 = getrmax(rmi5);
        System.out.println("ci");
        BigDecimal ci5 = getci(rmax5, rmi5.length);
        System.out.println();

        System.out.println("---------C2D1D2D3----------");
        double[][] C2D1D2D3 = {{1.0, 6.0, 9.0}, {1.0, 3.0}, {1.0}};
        double[][] arr6 = getArr(C2D1D2D3, df);
        System.out.println("wi");
        System.out.println();
        BigDecimal[] wi6 = getWi(arr6);
        System.out.println("wi0");
        System.out.println();
        BigDecimal[] wi06 = getWi0(wi6);
        System.out.println("rmi");
        BigDecimal[] rmi6 = getrmi(wi06, arr6);
        System.out.println("rmax");
        BigDecimal rmax6 = getrmax(rmi6);
        System.out.println("ci");
        BigDecimal ci6 = getci(rmax6, rmi6.length);
        System.out.println();

        System.out.println("---------C3D1D2D3----------");
        double[][] C3D1D2D3 = {{1.0, 3.0, 1.0/9.0}, {1.0, 1.0/7.0}, {1.0}};
        double[][] arr7 = getArr(C3D1D2D3, df);
        System.out.println("wi");
        System.out.println();
        BigDecimal[] wi7 = getWi(arr7);
        System.out.println("wi0");
        System.out.println();
        BigDecimal[] wi07 = getWi0(wi7);
        System.out.println("rmi");
        BigDecimal[] rmi7 = getrmi(wi07, arr7);
        System.out.println("rmax");
        BigDecimal rmax7 = getrmax(rmi7);
        System.out.println("ci");
        BigDecimal ci7 = getci(rmax7, rmi7.length);
        System.out.println();

        System.out.println("---------C4D1D2D3----------");
        double[][] C4D1D2D3 = {{1.0, 3.0, 1.0/4.0}, {1.0, 1.0/3.0}, {1.0}};
        double[][] arr8 = getArr(C4D1D2D3, df);
        System.out.println("wi");
        System.out.println();
        BigDecimal[] wi8 = getWi(arr8);
        System.out.println("wi0");
        System.out.println();
        BigDecimal[] wi08 = getWi0(wi8);
        System.out.println("rmi");
        BigDecimal[] rmi8 = getrmi(wi08, arr8);
        System.out.println("rmax");
        BigDecimal rmax8 = getrmax(rmi8);
        System.out.println("ci");
        BigDecimal ci8 = getci(rmax8, rmi8.length);
        System.out.println();

        System.out.println("---------C5D1D2D3----------");
        double[][] C5D1D2D3 = {{1.0, 7.0, 9.0}, {1.0, 3.0}, {1.0}};
        double[][] arr9 = getArr(C5D1D2D3, df);
        System.out.println("wi");
        System.out.println();
        BigDecimal[] wi9 = getWi(arr9);
        System.out.println("wi0");
        System.out.println();
        BigDecimal[] wi09 = getWi0(wi9);
        System.out.println("rmi");
        BigDecimal[] rmi9 = getrmi(wi09, arr9);
        System.out.println("rmax");
        BigDecimal rmax9 = getrmax(rmi9);
        System.out.println("ci");
        BigDecimal ci9 = getci(rmax9, rmi9.length);
        System.out.println();

        System.out.println("---------C6D1D2D3----------");
        double[][] C6D1D2D3 = {{1.0, 7.0, 9.0}, {1.0, 3.0}, {1.0}};
        double[][] arr10 = getArr(C6D1D2D3, df);
        System.out.println("wi");
        System.out.println();
        BigDecimal[] wi10 = getWi(arr10);
        System.out.println("wi0");
        System.out.println();
        BigDecimal[] wi010 = getWi0(wi10);
        System.out.println("rmi");
        BigDecimal[] rmi10 = getrmi(wi010, arr10);
        System.out.println("rmax");
        BigDecimal rmax10 = getrmax(rmi10);
        System.out.println("ci");
        BigDecimal ci10 = getci(rmax10, rmi10.length);
        System.out.println();

        System.out.println("---------C7D1D2D3----------");
        double[][] C7D1D2D3 = {{1.0, 3.0, 1.0/3.0}, {1.0, 1.0/6.0}, {1.0}};
        double[][] arr11 = getArr(C7D1D2D3, df);
        System.out.println("wi");
        System.out.println();
        BigDecimal[] wi11 = getWi(arr11);
        System.out.println("wi0");
        System.out.println();
        BigDecimal[] wi011 = getWi0(wi11);
        System.out.println("rmi");
        BigDecimal[] rmi11 = getrmi(wi011, arr11);
        System.out.println("rmax");
        BigDecimal rmax11 = getrmax(rmi11);
        System.out.println("ci");
        BigDecimal ci11 = getci(rmax11, rmi11.length);
        System.out.println();

        System.out.println("---------C8D1D2D3----------");
        double[][] C8D1D2D3 = {{1.0, 3.0, 1.0/4.0}, {1.0, 6.0}, {1.0}};
        double[][] arr12 = getArr(C8D1D2D3, df);
        System.out.println("wi");
        System.out.println();
        BigDecimal[] wi12 = getWi(arr12);
        System.out.println("wi0");
        System.out.println();
        BigDecimal[] wi012 = getWi0(wi12);
        System.out.println("rmi");
        BigDecimal[] rmi12 = getrmi(wi012, arr12);
        System.out.println("rmax");
        BigDecimal rmax12 = getrmax(rmi12);
        System.out.println("ci");
        BigDecimal ci12 = getci(rmax12, rmi12.length);
        System.out.println();

        System.out.println("---------C9D1D2D3----------");
        double[][] C9D1D2D3 = {{1.0, 5.0, 9.0}, {1.0, 1.0/3.0}, {1.0}};
        double[][] arr13 = getArr(C9D1D2D3, df);
        System.out.println("wi");
        System.out.println();
        BigDecimal[] wi13 = getWi(arr13);
        System.out.println("wi0");
        System.out.println();
        BigDecimal[] wi013 = getWi0(wi13);
        System.out.println("rmi");
        BigDecimal[] rmi13 = getrmi(wi013, arr13);
        System.out.println("rmax");
        BigDecimal rmax13 = getrmax(rmi13);
        System.out.println("ci");
        BigDecimal ci13 = getci(rmax13, rmi13.length);
        System.out.println();

    }

    private static BigDecimal getci(BigDecimal rmax, int length) {
        BigDecimal result = new BigDecimal(0);
        result = rmax.subtract(new BigDecimal(length));
        result = result.divide(new BigDecimal(length - 1), 3);
        System.out.println(result);
        return result;
    }

    private static BigDecimal getrmax(BigDecimal[] rmi1) {
        BigDecimal result = new BigDecimal(0);
        BigDecimal length = new BigDecimal(rmi1.length);
        for (BigDecimal bi : rmi1) {
            result = result.add(bi);
        }
        result = result.divide(length, 3);
        System.out.println(result);
        return result;
    }

    private static BigDecimal[] getrmi(BigDecimal[] wi0, double[][] arr) {
        int length = wi0.length;
        BigDecimal[] result = new BigDecimal[length];
        for (int i= 0; i < length; i++) {
            double[] temp = arr[i];
            BigDecimal b = new BigDecimal(0);
            for (int j = 0; j < length; j++) {
                BigDecimal big = new BigDecimal(temp[j]);
                b = b.add(big.multiply(wi0[j]));
            }
            b = b.divide(wi0[i], 3);
            System.out.println(b);
            result[i] = b;
        }
        return result;
    }

    private static BigDecimal[] getWi0(BigDecimal[] wi1) {
        int length = wi1.length;
        BigDecimal[] result = new BigDecimal[length];
        BigDecimal all = new BigDecimal(0);
        for (BigDecimal b : wi1) {
            all = all.add(b);
        }
        int i = 0;
        for (BigDecimal b : wi1) {
            result[i] = b.divide(all, 3);
            System.out.println(result[i]);
            i++;
        }
        return result;
    }

    private static BigDecimal[] getWi(double[][] arr) {
        int length = arr.length;
        BigDecimal[] result = new BigDecimal[length];
        int i= 0;
        for (double[] temp : arr) {
            BigDecimal b = new BigDecimal(1);
            for (double dou : temp) {
                b = b.multiply(new BigDecimal(dou));
            }
            result[i] = new BigDecimal(Math.pow(b.doubleValue(), 1.0 / length));
            System.out.println(result[i]);
            i++;
        }
        return result;
    }

    private static double[][] getArr(double[][] a, DecimalFormat df) {
        int length = a.length;
        double[][] arr = new double[length][length];
        for (int i = 0; i < length; i++) {
            if (i == 0) {
                arr[i] = a[i];
            } else {
                for (int z = i; z < length; z++) {
                    arr[i][z] = a[i][z - i];
                }
            }
            for (int j = 0; j < i; j ++) {
                arr[i][j] = 1.0 / a[j][i - j];
            }
        }
        format(arr, df);
        printArr(arr);
        return arr;
    }

    private static void format(double[][] arr, DecimalFormat df) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                arr[i][j] = Double.valueOf(df.format(arr[i][j]));
            }
        }
    }

    private static void printArr(double[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                System.out.print(arr[i][j] + "\t\t");
            }
            System.out.println();
        }
    }





}
