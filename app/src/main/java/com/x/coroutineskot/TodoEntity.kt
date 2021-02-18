package com.x.coroutineskot

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(primaryKeys = ["id"])
class TodoEntity() : Parcelable {
    lateinit var id: Integer


    lateinit var title: String

    constructor(parcel: Parcel) : this() {
        id = parcel.readValue(java.lang.Integer::class.java.classLoader) as Integer
        title = parcel.readString()!!

    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(id)
        dest.writeString(this.title)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TodoEntity> {
        override fun createFromParcel(parcel: Parcel): TodoEntity {
            return TodoEntity(parcel)
        }

        override fun newArray(size: Int): Array<TodoEntity?> {
            return arrayOfNulls(size)
        }
    }

}