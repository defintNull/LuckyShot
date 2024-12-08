package org.luckyshot.Models;

import jakarta.persistence.*;

@Entity
@Table(name = "prova")
public class Prova {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "string")
    private String string;

    @Column(name = "ciao")
    private int ciao;

    public Prova() {}

    public Prova(String string, int ciao) {
        this.string = string;
        this.ciao = ciao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public int getCiao() {
        return ciao;
    }

    public void setCiao(int ciao) {
        this.ciao = ciao;
    }
}
