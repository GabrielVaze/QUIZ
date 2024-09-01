package br.com.softwareminas.quizzzz.util;

public class NomeValor {
        private String mName;
        private String mValue;

        public NomeValor(String name, String value) {
            mName = name;
            mValue = value;
        }

        public String getName() {
            return mName;
        }

        public String getValue() {
            return mValue;
        }
}
