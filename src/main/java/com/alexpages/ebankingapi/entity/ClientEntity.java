package com.alexpages.ebankingapi.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.alexpages.ebankingapi.others.ClientRole;
import com.caixabank.absis.apps.dataservice.cbk.itmanagement.arcchn.entity.Column;
import com.caixabank.absis.apps.dataservice.cbk.itmanagement.arcchn.entity.GeneratedValue;
import com.caixabank.absis.apps.dataservice.cbk.itmanagement.arcchn.entity.Id;
import com.caixabank.absis.apps.dataservice.cbk.itmanagement.arcchn.entity.SequenceGenerator;

import java.util.Collection;
import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
@Entity
@Table(name = "TDE_CLIENT")
public class ClientEntity implements UserDetails {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CLIENT")
    @SequenceGenerator(sequenceName = "SEQ_CLIENT", allocationSize = 1, name = "SEQ_CLIENT")
	@Column(name = "ID")
    private Integer id;
    
	@Column(name = "NAME", unique = true)
    private String name;
   
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "client_id")
    private List<AccountEntity> accounts;
    
    @Column(name = "PASSWORD")
    private String password;
    
    
    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE")
    private ClientRole clientRole;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(clientRole.name()));
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
