package com.example.online.shopping.refresh;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RefreshEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refreshId;

    @Column
    @Setter
    private String refresh;

    @Column
    @Setter
    private String username;

    @Column
    @Setter
    private String role;

    @Setter
    private Long expiredMs;
}
