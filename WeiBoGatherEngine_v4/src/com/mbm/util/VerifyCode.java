package com.mbm.util;

import java.util.Random;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.Color;

/**
 * ��֤��������࣬��������֡���д��Сд��ĸ�����߻�����͵���֤�롣 ֧���Զ�����֤���ַ������� ֧���Զ�����֤��ͼƬ�Ĵ�С�� ֧���Զ������ų�������ַ�
 * ֧���Զ�������ߵ������� ֧���Զ�����֤��ͼ����ɫ
 * 
 * @author org.javachina
 * @version 1.01
 */
public class VerifyCode {

    /**
     * ��֤������Ϊ������ 0~9
     */
    public static final int TYPE_NUM_ONLY = 0;

    /**
     * ��֤������Ϊ����ĸ������д��Сд��ĸ���
     */
    public static final int TYPE_LETTER_ONLY = 1;

    /**
     * ��֤������Ϊ���֡���д��ĸ��Сд��ĸ���
     */
    public static final int TYPE_ALL_MIXED = 2;

    /**
     * ��֤������Ϊ���֡���д��ĸ���
     */
    public static final int TYPE_NUM_UPPER = 3;

    /**
     * ��֤������Ϊ���֡�Сд��ĸ���
     */
    public static final int TYPE_NUM_LOWER = 4;

    /**
     * ��֤������Ϊ����д��ĸ
     */
    public static final int TYPE_UPPER_ONLY = 5;

    /**
     * ��֤������Ϊ��Сд��ĸ
     */
    public static final int TYPE_LOWER_ONLY = 6;

    private VerifyCode() {
    }

    /**
     * �����֤���ַ�
     * 
     * @param type
     *            ��֤�����ͣ��μ���ľ�̬����
     * @param length
     *            ��֤�볤�ȣ�����0������
     * @param exChars
     *            ���ų�������ַ�������֡���ĸ�������֤����Ч�������ų���Ϊ null��
     * @return ��֤���ַ�
     */
    public static String generateTextCode(int type, int length, String exChars) {

        if (length <= 0)
            return "";

        StringBuffer code = new StringBuffer();
        int i = 0;
        Random r = new Random();

        switch (type) {

        // ������
        case TYPE_NUM_ONLY:
            while (i < length) {
                int t = r.nextInt(10);
                if (exChars == null || exChars.indexOf(t + "") < 0) {// �ų������ַ�
                    code.append(t);
                    i++;
                }
            }
            break;

        // ����ĸ������д��ĸ��Сд��ĸ��ϣ�
        case TYPE_LETTER_ONLY:
            while (i < length) {
                int t = r.nextInt(123);
                if ((t >= 97 || (t >= 65 && t <= 90)) && (exChars == null || exChars.indexOf((char) t) < 0)) {
                    code.append((char) t);
                    i++;
                }
            }
            break;

        // ���֡���д��ĸ��Сд��ĸ���
        case TYPE_ALL_MIXED:
            while (i < length) {
                int t = r.nextInt(123);
                if ((t >= 97 || (t >= 65 && t <= 90) || (t >= 48 && t <= 57)) && (exChars == null || exChars.indexOf((char) t) < 0)) {
                    code.append((char) t);
                    i++;
                }
            }
            break;

        // ���֡���д��ĸ���
        case TYPE_NUM_UPPER:
            while (i < length) {
                int t = r.nextInt(91);
                if ((t >= 65 || (t >= 48 && t <= 57)) && (exChars == null || exChars.indexOf((char) t) < 0)) {
                    code.append((char) t);
                    i++;
                }
            }
            break;

        // ���֡�Сд��ĸ���
        case TYPE_NUM_LOWER:
            while (i < length) {
                int t = r.nextInt(123);
                if ((t >= 97 || (t >= 48 && t <= 57)) && (exChars == null || exChars.indexOf((char) t) < 0)) {
                    code.append((char) t);
                    i++;
                }
            }
            break;

        // ����д��ĸ
        case TYPE_UPPER_ONLY:
            while (i < length) {
                int t = r.nextInt(91);
                if ((t >= 65) && (exChars == null || exChars.indexOf((char) t) < 0)) {
                    code.append((char) t);
                    i++;
                }
            }
            break;

        // ��Сд��ĸ
        case TYPE_LOWER_ONLY:
            while (i < length) {
                int t = r.nextInt(123);
                if ((t >= 97) && (exChars == null || exChars.indexOf((char) t) < 0)) {
                    code.append((char) t);
                    i++;
                }
            }
            break;

        }

        return code.toString();
    }

    /**
     * ������֤�룬�����֤��ͼƬ
     * 
     * @param textCode
     *            �ı���֤��
     * @param width
     *            ͼƬ���
     * @param height
     *            ͼƬ�߶�
     * @param interLine
     *            ͼƬ�и����ߵ�����
     * @param randomLocation
     *            ÿ���ַ�ĸߵ�λ���Ƿ����
     * @param backColor
     *            ͼƬ��ɫ����Ϊnull������������ɫ
     * @param foreColor
     *            ������ɫ����Ϊnull������������ɫ
     * @param lineColor
     *            ��������ɫ����Ϊnull������������ɫ
     * @return ͼƬ�������
     */
    public static BufferedImage generateImageCode(String textCode, int width, int height, int interLine, boolean randomLocation, Color backColor,
            Color foreColor, Color lineColor) {

        BufferedImage bim = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = bim.getGraphics();

        // ������ͼ
        g.setColor(backColor == null ? getRandomColor() : backColor);
        g.fillRect(0, 0, width, height);

        // ��������
        Random r = new Random();
        if (interLine > 0) {

            int x = 0, y = 0, x1 = width, y1 = 0;
            for (int i = 0; i < interLine; i++) {
                g.setColor(lineColor == null ? getRandomColor() : lineColor);
                y = r.nextInt(height);
                y1 = r.nextInt(height);

                g.drawLine(x, y, x1, y1);
            }
        }

        // д��֤��

        // g.setColor(getRandomColor());
        // g.setColor(isSimpleColor?Color.BLACK:Color.WHITE);

        // �����СΪͼƬ�߶ȵ�80%
        int fsize = (int) (height * 0.8);
        int fx = height - fsize;
        int fy = fsize;

        g.setFont(new Font(Font.DIALOG, Font.PLAIN, fsize));

        // д��֤���ַ�
        for (int i = 0; i < textCode.length(); i++) {
            fy = randomLocation ? (int) ((Math.random() * 0.3 + 0.6) * height) : fy;// ÿ���ַ�ߵ��Ƿ����
            g.setColor(foreColor == null ? getRandomColor() : foreColor);
            g.drawString(textCode.charAt(i) + "", fx, fy);
            fx += fsize * 0.9;
        }

        g.dispose();

        return bim;
    }

    /**
     * ���ͼƬ��֤��
     * 
     * @param type
     *            ��֤�����ͣ��μ���ľ�̬����
     * @param length
     *            ��֤���ַ�ȣ�����0������
     * @param exChars
     *            ���ų�������ַ�
     * @param width
     *            ͼƬ���
     * @param height
     *            ͼƬ�߶�
     * @param interLine
     *            ͼƬ�и����ߵ�����
     * @param randomLocation
     *            ÿ���ַ�ĸߵ�λ���Ƿ����
     * @param backColor
     *            ͼƬ��ɫ����Ϊnull������������ɫ
     * @param foreColor
     *            ������ɫ����Ϊnull������������ɫ
     * @param lineColor
     *            ��������ɫ����Ϊnull������������ɫ
     * @return ͼƬ�������
     */
    public static BufferedImage generateImageCode(int type, int length, String exChars, int width, int height, int interLine, boolean randomLocation,
            Color backColor, Color foreColor, Color lineColor) {

        String textCode = generateTextCode(type, length, exChars);
        BufferedImage bim = generateImageCode(textCode, width, height, interLine, randomLocation, backColor, foreColor, lineColor);

        return bim;
    }

    /**
     * ���������ɫ
     * 
     * @return
     */
    private static Color getRandomColor() {
        Random r = new Random();
        Color c = new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));
        return c;
    }

    public static void main(String[] args) {

    }
}
