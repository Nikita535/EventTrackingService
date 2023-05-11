package rtuit.lab.Models;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Table(name = "user")
@Entity
@NoArgsConstructor
@Getter
@AllArgsConstructor
@Setter
@Builder
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name")
    private String username;
    @Column(name = "password")
    private String password;

    @Column(name = "email",unique = true)
    private String email;

    @Column(name="active")
    private Boolean active;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> authorities = new HashSet<>();

    @OneToOne(targetEntity = Media.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "avatar_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "bytes", allowSetters = true)
    private Media avatar;



    public User(String email, String username, String password, Set<Role> authorities, Boolean active) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.authorities = authorities;
        this.active = active;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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