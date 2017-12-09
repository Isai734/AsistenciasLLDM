package col10dejunio.lldm.asistenciaslldm.view.items;

/**
 * Created by Isai on 23/02/2017.
 */

public class MemberItem {

    public final long id;
    public final String Nombre;
    public final String APaterno;
    public final String AMaterno;
    public final String Grupo;
    public final long IDLista;

    public MemberItem(long id, String nombre, String APaterno, String AMaterno, String grupo, long IDLista) {
        this.id = id;
        Nombre = nombre;
        this.APaterno = APaterno;
        this.AMaterno = AMaterno;
        Grupo = grupo;
        this.IDLista = IDLista;
    }

    public long getId() {
        return id;
    }

    public String getNombre() {
        return Nombre;
    }

    public String getAPaterno() {
        return APaterno;
    }

    public String getAMaterno() {
        return AMaterno;
    }

    public String getGrupo() {
        return Grupo;
    }

    public long getIDLista() {
        return IDLista;
    }
}
