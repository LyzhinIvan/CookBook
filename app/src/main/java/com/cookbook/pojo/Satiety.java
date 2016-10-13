package com.cookbook.pojo;

public enum Satiety {

    None(0) {
        @Override
        public String toString() {
            return "Сытность не указана";
        }
    },
    Light(1) {
        @Override
        public String toString() {
            return "Легкое";
        }
    },
    Medium(2) {
        @Override
        public String toString() {
            return "Среднее";
        }
    },
    Nourishing(3) {
        @Override
        public String toString() {
            return "Сытное";
        }
    };

    private int value;
    Satiety(int value) {
        this.value = value;
    }

    public static Satiety fromInt(int v) {
        switch (v) {
            case 0: return None;
            case 1: return Light;
            case 2: return Medium;
            case 3: return Nourishing;
            default:
                throw new IllegalArgumentException("v = "+v);
        }
    }

    public int getValue() {return value; }
}
