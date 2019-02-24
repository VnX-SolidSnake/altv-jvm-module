package alt.v.jvm;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import alt.v.jvm.CAPI;
import alt.v.jvm.CAPI.alt_event_type_t;
import jnr.ffi.Pointer;
import jnr.ffi.Struct;
import jnr.ffi.types.uintptr_t;

public class Main {
    public static Pointer server;
    public static void main(@uintptr_t long serverptr)
    {
        CAPI.server = Pointer.wrap(CAPI.runtime, serverptr);
        server = CAPI.server;
        CAPI.func.alt_server_log_info(server, "[JVM] Module successfully loaded");

        Plugins.Load();
        
        CAPI.func.alt_server_subscribe_event(server, alt_event_type_t.PLAYER_CONNECT, OnConnect);
    }

    static CAPI.alt_event_callback_t OnConnect = new CAPI.alt_event_callback_t()
    {
        public boolean callback(Pointer event) {
            Pointer ply = CAPI.func.alt_player_connect_event_get_target(event);
            ByteBuffer buffer = ByteBuffer.allocate(50);
            Pointer bufferptr = Pointer.wrap(CAPI.runtime, buffer);
            CAPI.func.alt_player_get_name(ply, bufferptr);
            String plyname = new String(buffer.array(), StandardCharsets.UTF_8).trim();
            CAPI.func.alt_server_log_info(server, "PLAYER "+plyname+" CONNECTED");

            // Set pos
            CAPI.func.alt_server_log_info(server, "SETTING POS");
            CAPI.alt_position_t pos = new CAPI.alt_position_t();
            pos.x.set(1500); pos.y.set(3200); pos.z.set(40);
            CAPI.func.alt_player_set_position(ply, Struct.getMemory(pos));

            // create veh
            CAPI.func.alt_server_log_info(server, "CREATING VEH");
            int vehhash = CAPI.func.alt_server_hash(server, "deluxo");
            CAPI.func.alt_server_create_vehicle(server, vehhash, Struct.getMemory(pos), 0);

            return true;
        }
    };
}
