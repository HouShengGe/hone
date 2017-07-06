package com.miaolian.facead.model;

/**
 * Created by gaofeng on 2017-03-06.
 */

public class OCRResponse {
    private Name Name;

    private Issue Issue;

    private Birt Birt;

    private Num Num;

    private Folk Folk;

    private Valid Valid;

    private Addr Addr;

    private Sex Sex;

    public Name getName() {
        return Name;
    }

    public void setName(Name Name) {
        this.Name = Name;
    }

    public Issue getIssue() {
        return Issue;
    }

    public void setIssue(Issue Issue) {
        this.Issue = Issue;
    }

    public Birt getBirt() {
        return Birt;
    }

    public void setBirt(Birt Birt) {
        this.Birt = Birt;
    }

    public Num getNum() {
        return Num;
    }

    public void setNum(Num Num) {
        this.Num = Num;
    }

    public Folk getFolk() {
        return Folk;
    }

    public void setFolk(Folk Folk) {
        this.Folk = Folk;
    }

    public Valid getValid() {
        return Valid;
    }

    public void setValid(Valid Valid) {
        this.Valid = Valid;
    }

    public Addr getAddr() {
        return Addr;
    }

    public void setAddr(Addr Addr) {
        this.Addr = Addr;
    }

    public Sex getSex() {
        return Sex;
    }

    public void setSex(Sex Sex) {
        this.Sex = Sex;
    }

    @Override
    public String toString() {
        return "ClassPojo [Name = " + Name + ", Issue = " + Issue + ", Birt = " + Birt + ", Num = " + Num + ", Folk = " + Folk + ", Valid = " + Valid + ", Addr = " + Addr + ", Sex = " + Sex + "]";
    }

    public static class Valid {
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "ClassPojo [value = " + value + "]";
        }
    }

    public static class Issue {
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "ClassPojo [value = " + value + "]";
        }
    }

    public static class Num {
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "ClassPojo [value = " + value + "]";
        }
    }

    public static class Addr {
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "ClassPojo [value = " + value + "]";
        }
    }

    public static class Birt {
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "ClassPojo [value = " + value + "]";
        }
    }

    public static class Folk {
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "ClassPojo [value = " + value + "]";
        }
    }

    public static class Sex {
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "ClassPojo [value = " + value + "]";
        }
    }

    public static class Name {
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "ClassPojo [value = " + value + "]";
        }
    }

}
