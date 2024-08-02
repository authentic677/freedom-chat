import mitt from "mitt";

export function displayTime(d) {
    const now=new Date()
    const year=now.getFullYear()
    const month=now.getMonth()+1
    const date=now.getDate()
    const hour=now.getHours()
    const minute=now.getMinutes()
    const second=now.getSeconds()
    const week=now.getDay()

    //同一天
    let flag=((year==d.getFullYear())&&(month==d.getMonth()+1)&&(date==d.getDate()))
    if(flag){
        return `${d.getHours().toString().padStart(2,'0')}:${d.getMinutes().toString().padStart(2,'0')}`
    }
    //昨天
    let d2=new Date(d)
    d2.setDate(d2.getDate()+1)
    flag=((year==d2.getFullYear())&&(month==d2.getMonth()+1)&&(date==d2.getDate()))
    if(flag){
        return `昨天 ${d.getHours().toString().padStart(2,'0')}:${d.getMinutes().toString().padStart(2,'0')}`
    }
    //同年
    flag=year==d.getFullYear()
    if(flag){
        return `${(d.getMonth()+1).toString().padStart(2,'0')}-${d.getDate().toString().padStart(2,'0')} ${d.getHours().toString().padStart(2,'0')}:${d.getMinutes().toString().padStart(2,'0')}`
    }
    //完整的
    return `${d.getFullYear()}-${(d.getMonth()+1).toString().padStart(2,'0')}-${d.getDate().toString().padStart(2,'0')}`
}

export let emitter=mitt()

export function formatFileSize(size) {
    const units = ['B', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];
    let index = 0;

    while (size >= 1024 && index < units.length - 1) {
        size /= 1024;
        index++;
    }

    return `${size.toFixed(2)} ${units[index]}`;
}