package com.alexpages.ebankingapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor @Builder @Entity 
@Table(name = "TDE_CLIENT")
public class ClientEntity implements UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CLIENT")
    @SequenceGenerator(sequenceName = "SEQ_CLIENT", allocationSize = 1, name = "SEQ_CLIENT")
	@Column(name = "ID")
    private Integer clientId;
    
	@Column(name = "NAME", unique = true)
	@Size(max = 50)
    private String name;
	
    @Column(name = "PASSWORD")
    @NotBlank
    private String password;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE")
    private String role;
   
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "CLIENT_ID")
    private List<AccountEntity> accounts;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}