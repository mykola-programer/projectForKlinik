package ua.nike.project.hibernate.type;

public enum Sex {
    M,
    W;

    public static Sex getInstance(Character symbol) {
        if (symbol == null) return null;
        switch (symbol) {
            case 'Ж':
            case 'ж':
                return Sex.W;
            case 'Ч':
            case 'ч':
            default:
                return Sex.M;
        }
    }

    public Character toCharacter() {
        switch (this.toString().charAt(0)) {
            case 'W':
            case 'w':
                return 'Ж';
            case 'M':
            case 'm':
            default:
                return 'Ч';
        }
    }
}