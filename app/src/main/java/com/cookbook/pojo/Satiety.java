package com.cookbook.pojo;

public enum Satiety {

    None {
        @Override
        public String toString() {
            return "Сытность не указана";
        }
    },
    Light {
        @Override
        public String toString() {
            return "Легкое";
        }
    },
    Medium {
        @Override
        public String toString() {
            return "Среднее";
        }
    },
    Nourishing {
        @Override
        public String toString() {
            return "Сытное";
        }
    };
}
