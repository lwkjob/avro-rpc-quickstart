package example;

import example.avro.User;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;

/**
 * Created by dell-pc on 2015/12/17.
 */
public class TestArvo {

    //序列化，反序列化
    @Test
    public void test1() throws  Exception{
        User user1 = new User();
        user1.setName("Alyssa");
        user1.setFavoriteNumber(256);
        user1.setFavoriteColor("你猜我喜欢什么颜色");
        // Leave favorite color null

        // Alternate constructor
        User user2 = new User("Ben", 7, "red");

        // Construct via builder
        User user3 = User.newBuilder()
                .setName("Charlie")
                .setFavoriteColor("blue")
                .setFavoriteNumber(null)
                .build();

        // Serialize user1 and user2 to disk

        DatumWriter<User> userDatumWriter = new SpecificDatumWriter<User>(User.class);
        DataFileWriter<User> dataFileWriter = new DataFileWriter<User>(userDatumWriter);
        dataFileWriter.create(user1.getSchema(), new File("users.avro"));
        dataFileWriter.append(user1);
        dataFileWriter.append(user2);
        dataFileWriter.append(user3);
        dataFileWriter.close();

        // Deserialize Users from disk
        DatumReader<User> userDatumReader = new SpecificDatumReader<User>(User.class);
        DataFileReader<User> dataFileReader =
                new DataFileReader<User>(new File("users.avro"), userDatumReader);
        User user = null;
        while (dataFileReader.hasNext()) {
            // Reuse user object by passing it to next(). This saves us from
            // allocating and garbage collecting many objects for files with
            // many items.
            user = dataFileReader.next(user);
            System.out.println(user);
        }

    }

    //无java类,序列化和反序列化
    @Test
    public void test2() throws  Exception{
        //user.avsc放置在“resources/avro”目录下
        InputStream inputStream = ClassLoader.getSystemResourceAsStream("avro/user.avsc");
        Schema schema = new Schema.Parser().parse(inputStream);

        GenericRecord user = new GenericData.Record(schema);
        user.put("name", "张三");
        user.put("favorite_number", 30);
        user.put("favorite_color","zhangsan@*.com");

        File diskFile = new File("users2.avro");
        long length=diskFile.length();
        DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<GenericRecord>(schema);
        DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<GenericRecord>(datumWriter);
        if(length==0){
            dataFileWriter.create(schema, diskFile);
        }       else {
            dataFileWriter.appendTo(diskFile);
        }
        dataFileWriter.append(user);
        dataFileWriter.close();

        DatumReader<GenericRecord> datumReader = new GenericDatumReader<GenericRecord>(schema);
        DataFileReader<GenericRecord> dataFileReader = new DataFileReader<GenericRecord>(diskFile, datumReader);
        GenericRecord _current = null;
        while (dataFileReader.hasNext()) {
            _current = dataFileReader.next(_current);
            System.out.println(user);
        }

        dataFileReader.close();
    }
}
